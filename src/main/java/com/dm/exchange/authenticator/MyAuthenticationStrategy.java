package com.dm.exchange.authenticator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryLoader;
import org.apache.commons.dbutils.QueryRunner;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.owasp.esapi.User;
import org.owasp.esapi.contrib.spring.authenticator.AuthenticatedUser;
import org.owasp.esapi.contrib.spring.authenticator.AuthenticationStrategy;
import org.owasp.esapi.errors.AuthenticationAccountsException;
import org.owasp.esapi.errors.EncryptionException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.util.Assert;

import com.dm.Subject;

public class MyAuthenticationStrategy implements
        AuthenticationStrategy<Authentication>, InitializingBean,
        MessageSourceAware
{

    /**
     * The logger.
     */
    private final Logger logger = ESAPI.getLogger("Authenticator");

    // ~ Instance fields
    // ================================================================================================
    /**
     * The plaintext password used to perform {@link PasswordEncoder#isPasswordValid(String, String, Object)} on when the user is
     * not found to avoid SEC-2056.
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    protected MessageSourceAccessor messages = SpringSecurityMessageSource
            .getAccessor();
    private UserCache userCache = new NullUserCache();
    private boolean forcePrincipalAsString = false;

    private QueryRunner queryRunner;
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();
    private UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private SaltSource saltSource;

    /**
     * The password used to perform
     * {@link PasswordEncoder#isPasswordValid(String, String, Object)} on when
     * the user is not found to avoid SEC-2056. This is necessary, because some
     * {@link PasswordEncoder} implementations will short circuit if the
     * password is not in a valid format.
     */
    private String userNotFoundEncodedPassword;

    /**
     * The user map.
     */
    private Map<Long, User> userMap = new HashMap<Long, User>();

    // Map<User, List<String>>, where the strings are password hashes, with the
    // current hash in entry 0
    private Map<User, List<String>> passwordMap = new Hashtable<User, List<String>>();
    
    public MyAuthenticationStrategy(){
        setPasswordEncoder(new PlaintextPasswordEncoder());
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException
    {
        Assert.isInstanceOf(
                UsernamePasswordAuthenticationToken.class,
                authentication,
                messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.onlySupports",
                        "Only UsernamePasswordAuthenticationToken is supported"));

        // Determine username
        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED"
                : authentication.getName();

        boolean cacheWasUsed = true;
        UserDetails user = this.userCache.getUserFromCache(username);

        if (user == null)
        {
            cacheWasUsed = false;

            try
            {
                user = retrieveUser(username,
                        (UsernamePasswordAuthenticationToken) authentication);
            } catch (UsernameNotFoundException notFound)
            {
                logger.debug(Logger.SECURITY_FAILURE, "User '" + username
                        + "' not found");
                throw new BadCredentialsException(
                        messages.getMessage(
                                "AbstractUserDetailsAuthenticationProvider.badCredentials",
                                "Bad credentials"));
            }

            Assert.notNull(user,
                    "retrieveUser returned null - a violation of the interface contract");
        }

        try
        {
            preAuthenticationChecks.check(user);
            additionalAuthenticationChecks(user,
                    (UsernamePasswordAuthenticationToken) authentication);
        } catch (AuthenticationException exception)
        {
            if (cacheWasUsed)
            {
                // There was a problem, so try again after checking
                // we're using latest data (i.e. not from the cache)
                cacheWasUsed = false;
                user = retrieveUser(username,
                        (UsernamePasswordAuthenticationToken) authentication);
                preAuthenticationChecks.check(user);
                additionalAuthenticationChecks(user,
                        (UsernamePasswordAuthenticationToken) authentication);
            } else
            {
                throw exception;
            }
        }

        postAuthenticationChecks.check(user);

        if (!cacheWasUsed)
        {
            this.userCache.putUserInCache(user);
        }

        Object principalToReturn = user;

        if (forcePrincipalAsString)
        {
            principalToReturn = user.getUsername();
        }

        return createSuccessAuthentication(principalToReturn, authentication,
                user);
    }

    protected final UserDetails retrieveUser(String username,
            UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException
    {
        UserDetails loadedUser;

        try
        {
            loadedUser = this.getUserDetailsService().loadUserByUsername(
                    username);
        } catch (UsernameNotFoundException notFound)
        {
            if (authentication.getCredentials() != null)
            {
                String presentedPassword = authentication.getCredentials()
                        .toString();
                passwordEncoder.isPasswordValid(userNotFoundEncodedPassword,
                        presentedPassword, null);
            }
            throw notFound;
        } catch (Exception repositoryProblem)
        {
            throw new AuthenticationServiceException(
                    repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null)
        {
            throw new AuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
    }
    
    /**
     * Sets the PasswordEncoder instance to be used to encode and validate passwords.
     * If not set, the password will be compared as plain text.
     * <p>
     * For systems which are already using salted password which are encoded with a previous release, the encoder
     * should be of type {@code org.springframework.security.authentication.encoding.PasswordEncoder}. Otherwise,
     * the recommended approach is to use {@code org.springframework.security.crypto.password.PasswordEncoder}.
     *
     * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder} types.
     */
    public void setPasswordEncoder(Object passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");

        if (passwordEncoder instanceof PasswordEncoder) {
            setPasswordEncoder((PasswordEncoder) passwordEncoder);
            return;
        }

        if (passwordEncoder instanceof org.springframework.security.crypto.password.PasswordEncoder) {
            final org.springframework.security.crypto.password.PasswordEncoder delegate =
                    (org.springframework.security.crypto.password.PasswordEncoder)passwordEncoder;
            setPasswordEncoder(new PasswordEncoder() {
                public String encodePassword(String rawPass, Object salt) {
                    checkSalt(salt);
                    return delegate.encode(rawPass);
                }

                public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
                    checkSalt(salt);
                    return delegate.matches(rawPass, encPass);
                }

                private void checkSalt(Object salt) {
                    Assert.isNull(salt, "Salt value must be null when used with crypto module PasswordEncoder");
                }
            });

            return;
        }

        throw new IllegalArgumentException("passwordEncoder must be a PasswordEncoder instance");
    }

    private void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");

        //this.userNotFoundEncodedPassword = passwordEncoder.encodePassword(USER_NOT_FOUND_PASSWORD, null);
        this.passwordEncoder = passwordEncoder;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    // ~ Methods
    // ========================================================================================================

    @SuppressWarnings("deprecation")
    protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException
    {
        Object salt = null;

        if (this.saltSource != null)
        {
            salt = this.saltSource.getSalt(userDetails);
        }

        if (authentication.getCredentials() == null)
        {
            logger.debug(Logger.SECURITY_FAILURE,
                    "Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"), userDetails);
        }

        String presentedPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.isPasswordValid(userDetails.getPassword(),
                presentedPassword, salt))
        {
            logger.debug(Logger.SECURITY_FAILURE,
                    "Authentication failed: password does not match stored value");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"), userDetails);
        }
    }

    /**
     * Creates a successful {@link Authentication} object.
     * <p>
     * Protected so subclasses can override.
     * </p>
     * <p>
     * Subclasses will usually store the original credentials the user supplied
     * (not salted or encoded passwords) in the returned
     * <code>Authentication</code> object.
     * </p>
     * 
     * @param principal
     *            that should be the principal in the returned object (defined
     *            by the {@link #isForcePrincipalAsString()} method)
     * @param authentication
     *            that was presented to the provider for validation
     * @param user
     *            that was loaded by the implementation
     * 
     * @return the successful authentication token
     */
    protected Authentication createSuccessAuthentication(Object principal,
            Authentication authentication, UserDetails user)
    {
        // Ensure we return the original credentials the user supplied,
        // so subsequent attempts are successful even with encoded passwords.
        // Also ensure we return the original getDetails(), so that future
        // authentication events after cache expiry contain the details
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                principal, authentication.getCredentials(),
                authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());

        return result;
    }

    @Override
    public synchronized User createUser(String accountName, String password)
    {

        if (getUser(accountName) != null)
        {
            throw new RuntimeException(new AuthenticationAccountsException(
                    "Account creation failed",
                    "Duplicate user creation denied for " + accountName));
        }

        Subject subject = new Subject(accountName);
        AuthenticatedUser user = new AuthenticatedUser(subject);

        try
        {
            setHashedPassword(user, hashPassword(password, accountName));
        } catch (EncryptionException ee)
        {
            // throw new
            // org.owasp.esapi.errors.AuthenticationException("Internal error",
            // "Error hashing password for " + accountName, ee);
        }

        userMap.put(user.getAccountId(), user);
        saveUser(user);
        logger.info(Logger.SECURITY_SUCCESS, "New user created: " + accountName);
        return user;
    }

    public void saveUser(AuthenticatedUser user)
    {
        try
        {
            Map<String, String> queryMap = QueryLoader.instance().load(
                    "/Queries.properties");
            String sql = queryMap.get("DEF_CREATE_USER_SQL");
            queryRunner.update(sql, user.getAccountId(), user.getAccountName(),
                    getHashedPassword(user),
                    user.getExpirationTime().getTime(), user
                            .getFailedLoginCount(), user.getLastHostAddress(),
                    new java.sql.Date(user.getLastFailedLoginTime().getTime()),
                    new java.sql.Date(user.getLastLoginTime().getTime()),
                    new java.sql.Date(user.getLastPasswordChangeTime()
                            .getTime()), user.getScreenName(),
                    user.isEnabled(), user.isLocked());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public synchronized User getUser(String accountName)
    {
        if (accountName == null)
        {
            return User.ANONYMOUS;
        }
        for (User u : userMap.values())
        {
            if (u.getAccountName().equalsIgnoreCase(accountName))
            {
                return u;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws EncryptionException
     */
    public String hashPassword(String password, String accountName)
            throws EncryptionException
    {
        String salt = accountName.toLowerCase();
        return ESAPI.encryptor().hash(password, salt);
    }

    /**
     * Add a hash to a User's hashed password list. This method is used to store
     * a user's old password hashes to be sure that any new passwords are not
     * too similar to old passwords.
     * 
     * @param user
     *            the user to associate with the new hash
     * @param hash
     *            the hash to store in the user's password hash list
     */
    private void setHashedPassword(User user, String hash)
    {
        List<String> hashes = getAllHashedPasswords(user, true);
        hashes.add(0, hash);
        if (hashes.size() > ESAPI.securityConfiguration()
                .getMaxOldPasswordHashes())
        {
            hashes.remove(hashes.size() - 1);
        }
        logger.info(Logger.SECURITY_SUCCESS, "New hashed password stored for "
                + user.getAccountName());
    }

    /**
     * Return the specified User's current hashed password.
     * 
     * @param user
     *            this User's current hashed password will be returned
     * @return the specified User's current hashed password
     */
    String getHashedPassword(User user)
    {
        List hashes = getAllHashedPasswords(user, false);
        return (String) hashes.get(0);
    }

    /**
     * Returns all of the specified User's hashed passwords. If the User's list
     * of passwords is null, and create is set to true, an empty password list
     * will be associated with the specified User and then returned. If the
     * User's password map is null and create is set to false, an exception will
     * be thrown.
     * 
     * @param user
     *            the User whose old hashes should be returned
     * @param create
     *            true - if no password list is associated with this user,
     *            create one false - if no password list is associated with this
     *            user, do not create one
     * @return a List containing all of the specified User's password hashes
     */
    List<String> getAllHashedPasswords(User user, boolean create)
    {
        List<String> hashes = passwordMap.get(user);
        if (hashes != null)
        {
            return hashes;
        }
        if (create)
        {
            hashes = new ArrayList<String>();
            passwordMap.put(user, hashes);
            return hashes;
        }
        throw new RuntimeException("No hashes found for "
                + user.getAccountName()
                + ". Is User.hashcode() and equals() implemented correctly?");
    }

    @Override
    public void changePassword(String username, String newPassword)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteUser(String username)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean userExists(String username)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public final void afterPropertiesSet() throws Exception
    {
        Assert.notNull(this.userCache, "A user cache must be set");
        Assert.notNull(this.messages, "A message source must be set");
        doAfterPropertiesSet();
    }
    
    protected void doAfterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }
    
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setQueryRunner(QueryRunner queryRunner)
    {
        this.queryRunner = queryRunner;
    }
    
    /**
     * The source of salts to use when decoding passwords. <code>null</code>
     * is a valid value, meaning the <code>DaoAuthenticationProvider</code>
     * will present <code>null</code> to the relevant <code>PasswordEncoder</code>.
     * <p>
     * Instead, it is recommended that you use an encoder which uses a random salt and combines it with
     * the password field. This is the default approach taken in the
     * {@code org.springframework.security.crypto.password} package.
     *
     * @param saltSource to use when attempting to decode passwords via the <code>PasswordEncoder</code>
     */
    public void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }

    protected SaltSource getSaltSource() {
        return saltSource;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService)
    {
        this.userDetailsService = userDetailsService;
    }

    protected UserDetailsService getUserDetailsService()
    {
        return userDetailsService;
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker
    {
        public void check(UserDetails user)
        {
            if (!user.isAccountNonLocked())
            {
                logger.debug(Logger.SECURITY_FAILURE, "User account is locked");

                throw new LockedException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.locked",
                        "User account is locked"), user);
            }

            if (!user.isEnabled())
            {
                logger.debug(Logger.SECURITY_FAILURE,
                        "User account is disabled");

                throw new DisabledException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.disabled",
                        "User is disabled"), user);
            }

            if (!user.isAccountNonExpired())
            {
                logger.debug(Logger.SECURITY_FAILURE, "User account is expired");

                throw new AccountExpiredException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.expired",
                        "User account has expired"), user);
            }
        }
    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker
    {
        public void check(UserDetails user)
        {
            if (!user.isCredentialsNonExpired())
            {
                logger.debug(Logger.SECURITY_FAILURE,
                        "User account credentials have expired");

                throw new CredentialsExpiredException(
                        messages.getMessage(
                                "AbstractUserDetailsAuthenticationProvider.credentialsExpired",
                                "User credentials have expired"), user);
            }
        }
    }

}

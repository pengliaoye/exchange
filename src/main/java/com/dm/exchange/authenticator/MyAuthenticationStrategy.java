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
import org.owasp.esapi.errors.EncryptionException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.dm.Subject;

public class MyAuthenticationStrategy implements AuthenticationStrategy<String> {
	
    /**
     * The logger.
     */
    private final Logger logger = ESAPI.getLogger("Authenticator");
    
    private QueryRunner queryRunner;
    
    /**
     * The user map.
     */
    private Map<Long, User> userMap = new HashMap<Long, User>();
    
    // Map<User, List<String>>, where the strings are password hashes, with the current hash in entry 0
    private Map<User, List<String>> passwordMap = new Hashtable<User, List<String>>();

	@Override
	public String authenticate(Authentication authentication)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createUser(String accountName, String password) {
      
        if (getUser(accountName) != null) {
            //throw new AuthenticationAccountsException("Account creation failed", "Duplicate user creation denied for " + accountName);
        }      
        
        Subject subject = new Subject(accountName);
        AuthenticatedUser user = new AuthenticatedUser(subject);

        try {
            setHashedPassword(user, hashPassword(password, accountName));
        } catch (EncryptionException ee) {
           //throw new org.owasp.esapi.errors.AuthenticationException("Internal error", "Error hashing password for " + accountName, ee);
        }

        userMap.put(user.getAccountId(), user);
        saveUser(subject);
        logger.info(Logger.SECURITY_SUCCESS, "New user created: " + accountName);        
        return user;
	}
	
	public void saveUser(Subject subject){
		try{
			Map<String, String> queryMap = QueryLoader.instance().load("/Queries.properties");
			String sql = queryMap.get("DEF_CREATE_USER_SQL");
			queryRunner.update(sql, subject.getId(), subject.getAccountName(), subject.getPassword(), subject.getExpirationDate()
					,subject.getFailedLoginCount(), subject.getLastHostAddress(), subject.getLastFailedLoginTime(), subject.getLastLoginTime()
					,subject.getLastPasswordChangeTime(), subject.getScreenName(), subject.isEnabled(), subject.isLocked());
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public synchronized User getUser(String accountName) {
		return null;
	}
	
    /**
     * {@inheritDoc}
     *
     * @throws EncryptionException
     */
    public String hashPassword(String password, String accountName) throws EncryptionException {
        String salt = accountName.toLowerCase();
        return ESAPI.encryptor().hash(password, salt);
    }
    
    /**
     * Add a hash to a User's hashed password list.  This method is used to store a user's old password hashes
     * to be sure that any new passwords are not too similar to old passwords.
     *
     * @param user the user to associate with the new hash
     * @param hash the hash to store in the user's password hash list
     */
    private void setHashedPassword(User user, String hash) {
        List<String> hashes = getAllHashedPasswords(user, true);
        hashes.add(0, hash);
        if (hashes.size() > ESAPI.securityConfiguration().getMaxOldPasswordHashes()) {
            hashes.remove(hashes.size() - 1);
        }
        logger.info(Logger.SECURITY_SUCCESS, "New hashed password stored for " + user.getAccountName());
    }
    
    /**
     * Returns all of the specified User's hashed passwords.  If the User's list of passwords is null,
     * and create is set to true, an empty password list will be associated with the specified User
     * and then returned. If the User's password map is null and create is set to false, an exception
     * will be thrown.
     *
     * @param user   the User whose old hashes should be returned
     * @param create true - if no password list is associated with this user, create one
     *               false - if no password list is associated with this user, do not create one
     * @return a List containing all of the specified User's password hashes
     */
    List<String> getAllHashedPasswords(User user, boolean create) {
        List<String> hashes = passwordMap.get(user);
        if (hashes != null) {
            return hashes;
        }
        if (create) {
            hashes = new ArrayList<String>();
            passwordMap.put(user, hashes);
            return hashes;
        }
        throw new RuntimeException("No hashes found for " + user.getAccountName() + ". Is User.hashcode() and equals() implemented correctly?");
    }

	@Override
	public void changePassword(String username, String newPassword) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteUser(String username) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean userExists(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setQueryRunner(QueryRunner queryRunner) {
		this.queryRunner = queryRunner;
	}

}

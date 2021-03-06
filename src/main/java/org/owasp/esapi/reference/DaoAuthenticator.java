package org.owasp.esapi.reference;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.owasp.esapi.Authenticator;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.EncoderConstants;
import org.owasp.esapi.Logger;
import org.owasp.esapi.Randomizer;
import org.owasp.esapi.StringUtilities;
import org.owasp.esapi.User;
import org.owasp.esapi.errors.AuthenticationAccountsException;
import org.owasp.esapi.errors.AuthenticationCredentialsException;
import org.owasp.esapi.errors.AuthenticationException;
import org.owasp.esapi.errors.EncryptionException;
import org.owasp.esapi.reference.crypto.EncryptedPropertiesUtils;
import org.springframework.util.ResourceUtils;

import com.dm.util.ConnUtil;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DaoAuthenticator extends AbstractAuthenticator {

  private static volatile Authenticator singletonInstance;

  public static Authenticator getInstance() {
    if (singletonInstance == null) {
      synchronized (DaoAuthenticator.class) {
        if (singletonInstance == null) {
          singletonInstance = new DaoAuthenticator();
        }
      }
    }
    return singletonInstance;
  }

  /**
   * The logger.
   */
  private final Logger logger = ESAPI.getLogger("Authenticator");

  private static final int MAX_ACCOUNT_NAME_LENGTH = 250;

  /**
   * Fail safe main program to add or update an account in an emergency.
   * <p/>
   * Warning: this method does not perform the level of validation and checks generally required in
   * ESAPI, and can therefore be used to create a username and password that do not comply with the
   * username and password strength requirements.
   * <p/>
   * Example: Use this to add the alice account with the admin role to the users file:
   * 
   * <PRE>
   * <p/>
   * java -Dorg.owasp.esapi.resources="/path/resources" -classpath esapi.jar org.owasp.esapi.Authenticator alice password admin
   * <p/>
   * </PRE>
   *
   * @param args the arguments (username, password, role)
   * @throws Exception the exception
   */
  public static void main(String[] args) throws Exception {
    if (args.length != 3) {
      System.out.println("Usage: Authenticator accountname password role");
      return;
    }
    DaoAuthenticator auth = new DaoAuthenticator();
    String accountName = args[0].toLowerCase();
    String password = args[1];
    String role = args[2];
    DefaultUser user = (DefaultUser) auth.getUser(args[0]);
    if (user == null) {
      user = new DefaultUser(accountName);
      String newHash = auth.hashPassword(password, accountName);
      auth.setHashedPassword(user, newHash);
      user.addRole(role);
      user.enable();
      user.unlock();
      System.out.println("New user created: " + accountName);
      auth.saveUser(user, true);
      System.out.println("User account " + user.getAccountName() + " updated");
    } else {
      System.err.println("User account " + user.getAccountName() + " already exists!");
    }
  }

  /**
   * Add a hash to a User's hashed password list. This method is used to store a user's old password
   * hashes to be sure that any new passwords are not too similar to old passwords.
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
   * Return the specified User's current hashed password.
   *
   * @param user this User's current hashed password will be returned
   * @return the specified User's current hashed password
   */
  String getHashedPassword(User user) {
    List hashes = getAllHashedPasswords(user, false);
    return (String) hashes.get(0);
  }

  /**
   * Set the specified User's old password hashes. This will not set the User's current password
   * hash.
   *
   * @param user the User whose old password hashes will be set
   * @param oldHashes a list of the User's old password hashes *
   */
  void setOldPasswordHashes(User user, List<String> oldHashes) {
    List<String> hashes = getAllHashedPasswords(user, true);
    if (hashes.size() > 1) {
      hashes.removeAll(hashes.subList(1, hashes.size() - 1));
    }
    hashes.addAll(oldHashes);
  }

  /**
   * Returns all of the specified User's hashed passwords. If the User's list of passwords is null,
   * and create is set to true, an empty password list will be associated with the specified User
   * and then returned. If the User's password map is null and create is set to false, an exception
   * will be thrown.
   *
   * @param user the User whose old hashes should be returned
   * @param create true - if no password list is associated with this user, create one false - if no
   *        password list is associated with this user, do not create one
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
    throw new RuntimeException("No hashes found for " + user.getAccountName()
        + ". Is User.hashcode() and equals() implemented correctly?");
  }

  /**
   * Get a List of the specified User's old password hashes. This will not return the User's current
   * password hash.
   *
   * @param user he user whose old password hashes should be returned
   * @return the specified User's old password hashes
   */
  List<String> getOldPasswordHashes(User user) {
    List<String> hashes = getAllHashedPasswords(user, false);
    if (hashes.size() > 1) {
      return Collections.unmodifiableList(hashes.subList(1, hashes.size() - 1));
    }
    return Collections.emptyList();
  }

  // Map<User, List<String>>, where the strings are password hashes, with the current hash in entry
  // 0
  private Map<User, List<String>> passwordMap = new Hashtable<User, List<String>>();



  /**
     *
     */
  private DaoAuthenticator() {
    super();
  }


  /**
   * {@inheritDoc}
   */
  public synchronized User createUser(String accountName, String password1, String password2)
      throws AuthenticationException {

    if (accountName == null) {
      throw new AuthenticationAccountsException("Account creation failed",
          "Attempt to create user with null accountName");
    }
    if (getUser(accountName) != null) {
      throw new AuthenticationAccountsException("Account creation failed",
          "Duplicate user creation denied for " + accountName);
    }

    verifyAccountNameStrength(accountName);

    if (password1 == null) {
      throw new AuthenticationCredentialsException("Invalid account name",
          "Attempt to create account " + accountName + " with a null password");
    }

    DefaultUser user = new DefaultUser(accountName);

    verifyPasswordStrength(null, password1, user);

    if (!password1.equals(password2)) {
      throw new AuthenticationCredentialsException("Passwords do not match", "Passwords for "
          + accountName + " do not match");
    }

    try {
      setHashedPassword(user, hashPassword(password1, accountName));
    } catch (EncryptionException ee) {
      throw new AuthenticationException("Internal error", "Error hashing password for "
          + accountName, ee);
    }

    logger.info(Logger.SECURITY_SUCCESS, "New user created: " + accountName);
    saveUser(user, true);
    return user;
  }

  /**
   * {@inheritDoc}
   */
  public String generateStrongPassword() {
    return generateStrongPassword("");
  }

  /**
   * Generate a strong password that is not similar to the specified old password.
   *
   * @param oldPassword the password to be compared to the new password for similarity
   * @return a new strong password that is dissimilar to the specified old password
   */
  private String generateStrongPassword(String oldPassword) {
    Randomizer r = ESAPI.randomizer();
    int letters = r.getRandomInteger(4, 6); // inclusive, exclusive
    int digits = 7 - letters;
    String passLetters = r.getRandomString(letters, EncoderConstants.CHAR_PASSWORD_LETTERS);
    String passDigits = r.getRandomString(digits, EncoderConstants.CHAR_PASSWORD_DIGITS);
    String passSpecial = r.getRandomString(1, EncoderConstants.CHAR_PASSWORD_SPECIALS);
    String newPassword = passLetters + passSpecial + passDigits;
    if (StringUtilities.getLevenshteinDistance(oldPassword, newPassword) > 5) {
      return newPassword;
    }
    return generateStrongPassword(oldPassword);
  }

  /**
   * {@inheritDoc}
   */
  public void changePassword(User user, String currentPassword, String newPassword,
      String newPassword2) throws AuthenticationException {
    String accountName = user.getAccountName();
    try {
      String currentHash = getHashedPassword(user);
      String verifyHash = hashPassword(currentPassword, accountName);
      if (!currentHash.equals(verifyHash)) {
        throw new AuthenticationCredentialsException("Password change failed",
            "Authentication failed for password change on user: " + accountName);
      }
      if (newPassword == null || newPassword2 == null || !newPassword.equals(newPassword2)) {
        throw new AuthenticationCredentialsException("Password change failed",
            "Passwords do not match for password change on user: " + accountName);
      }
      verifyPasswordStrength(currentPassword, newPassword, user);
      user.setLastPasswordChangeTime(new Date());
      String newHash = hashPassword(newPassword, accountName);
      if (getOldPasswordHashes(user).contains(newHash)) {
        throw new AuthenticationCredentialsException("Password change failed",
            "Password change matches a recent password for user: " + accountName);
      }
      setHashedPassword(user, newHash);
      logger.info(Logger.SECURITY_SUCCESS, "Password changed for user: " + accountName);
      // jtm - 11/2/2010 - added to resolve
      // http://code.google.com/p/owasp-esapi-java/issues/detail?id=13
      saveUser(user, false);
    } catch (EncryptionException ee) {
      throw new AuthenticationException("Password change failed",
          "Encryption exception changing password for " + accountName, ee);
    }
  }

  /**
   * {@inheritDoc}
   */
  public boolean verifyPassword(User user, String password) {
    String accountName = user.getAccountName();
    try {
      String hash = hashPassword(password, accountName);
      String currentHash = getHashedPassword(user);
      if (hash.equals(currentHash)) {
        user.setLastLoginTime(new Date());
        ((DefaultUser) user).setFailedLoginCount(0);
        logger.info(Logger.SECURITY_SUCCESS, "Password verified for " + accountName);
        return true;
      }
    } catch (EncryptionException e) {
      logger.fatal(Logger.SECURITY_FAILURE, "Encryption error verifying password for "
          + accountName);
    }
    logger.fatal(Logger.SECURITY_FAILURE, "Password verification failed for " + accountName);
    return false;
  }

  /**
   * {@inheritDoc}
   */
  public String generateStrongPassword(User user, String oldPassword) {
    String newPassword = generateStrongPassword(oldPassword);
    if (newPassword != null) {
      logger
          .info(Logger.SECURITY_SUCCESS, "Generated strong password for " + user.getAccountName());
    }
    return newPassword;
  }

  /**
   * {@inheritDoc}
   */
  public synchronized User getUser(long accountId) {
    if (accountId == 0) {
      return User.ANONYMOUS;
    }
    try {
      User user = getUser("id", accountId);
      return user;
    } catch (Exception e) {
      logger.fatal(Logger.SECURITY_FAILURE, "Failure loading user " + accountId, e);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public synchronized User getUser(String accountName) {
    if (accountName == null) {
      return User.ANONYMOUS;
    }

    try {
      User user = getUser("account_name", accountName);
      return user;
    } catch (Exception e) {
      logger.fatal(Logger.SECURITY_FAILURE, "Failure loading user " + accountName, e);
    }
    return null;
  }

  public User getUser(String field, Object value) throws Exception {
    QueryRunner queryRunner = new QueryRunner();
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT id, account_name, password, expiration_time, failed_login_count,\n");
    builder
        .append("last_host_address, last_failed_login_time, last_login_time, last_password_change_time, \n");
    builder.append("screen_name, enabled, locked, roles, old_password\n");
    builder.append("FROM tb_users WHERE " + field + "=?\n");
    String sql = builder.toString();
    Connection conn = ConnUtil.getConn();
    DefaultUser user =
        queryRunner
            .query(
                conn,
                sql,
                (ResultSet rs) -> {
                  if (rs.next()) {
                    try {
                      long accountId = rs.getLong("id");
                      String accountName = rs.getString("account_name");
                      verifyAccountNameStrength(accountName);
                      DefaultUser user1 = new DefaultUser(accountName);
                      user1.setScreenName(rs.getString("screen_name"));
                      user1.accountId = accountId;
                      String password = rs.getString("password");
                      verifyPasswordStrength(null, password, user1);
                      setHashedPassword(user1, password);
                      String[] roles = rs.getString("roles").toLowerCase().split(" *, *");
                      for (String role : roles) {
                        if (!"".equals(role)) {
                          user1.addRole(role);
                        }
                      }
                      if (!"unlocked".equalsIgnoreCase(rs.getString("locked"))) {
                        user1.lock();
                      }
                      if ("enabled".equalsIgnoreCase(rs.getString("enabled"))) {
                        user1.enable();
                      } else {
                        user1.disable();
                      }
                      user1.resetCSRFToken();
                      setOldPasswordHashes(user1,
                          Arrays.asList(rs.getString("old_password").split(" *, *")));
                      user1.setLastHostAddress("null".equals(rs.getString("last_host_address")) ? null
                          : rs.getString("last_host_address"));
                      user1.setLastPasswordChangeTime(new Date(rs.getDate("last_password_change_time")
                          .getTime()));
                      user1.setLastLoginTime(new Date(rs.getDate("last_login_time").getTime()));
                      user1.setLastFailedLoginTime(new Date(rs.getDate("last_failed_login_time")
                          .getTime()));
                      user1.setExpirationTime(new Date(rs.getDate("expiration_time").getTime()));
                      user1.setFailedLoginCount(rs.getInt("failed_login_count"));
                      return user1;
                    } catch (AuthenticationException e) {
                      throw new RuntimeException(e);
                    }
                  } else {
                    return null;
                  }
                }, value);
    return user;
  }

  public synchronized Set getUserNames() {
    HashSet<String> results = new HashSet<String>();
    // TODO
    return results;
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
   * {@inheritDoc}
   */
  public synchronized void removeUser(String accountName) throws AuthenticationException {
    User user = getUser(accountName);
    if (user == null) {
      throw new AuthenticationAccountsException("Remove user failed",
          "Can't remove invalid accountName " + accountName);
    }
    logger.info(Logger.SECURITY_SUCCESS, "Removing user " + user.getAccountName());
    passwordMap.remove(user);
    Connection conn = null;
    try {
      conn = ConnUtil.getConn();
      QueryRunner queryRunner = new QueryRunner();
      queryRunner.update(conn, "delete from users where accountname = ?", accountName);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      DbUtils.closeQuietly(conn);
    }
  }

  /**
   * @throws AuthenticationException if the user file could not be written
   */
  public synchronized void saveUser(User user, boolean create) throws AuthenticationException {

    try {

      DefaultUser u = (DefaultUser) user;
      if (u != null && !u.isAnonymous()) {

        Connection conn = ConnUtil.getConn();
        QueryRunner queryRunner = new QueryRunner();
        try {
          StringBuilder builder = new StringBuilder();
          if (create) {
            builder
                .append("insert into tb_users (id,account_name,password,expiration_time,failed_login_count,\n");
            builder
                .append("last_host_address,last_failed_login_time,last_login_time,last_password_change_time,screen_name,enabled,locked,roles,old_password)\n");
            builder.append("values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
          } else {
            builder
                .append("update users set account_name=?,password=?,expiration_time=?,failed_login_count=?,last_host_address=?,last_failed_login_time=?,\n");
            builder
                .append("last_login_time=?,last_password_change_time=?,screen_name=?,enabled=?,locked=?,roles=?,old_password=? where id = ?");
          }
          String sql = builder.toString();
          Object[] params =
              new Object[] {null, user.getAccountName(), getHashedPassword(user),
                  Timestamp.valueOf(LocalDateTime.now().withYear(9999)), user.getFailedLoginCount(),
                  user.getLastHostAddress(),
                  new java.sql.Timestamp(user.getLastFailedLoginTime().getTime()),
                  new java.sql.Timestamp(user.getLastLoginTime().getTime()),
                  new java.sql.Timestamp(user.getLastPasswordChangeTime().getTime()),
                  user.getScreenName(), user.isEnabled() ? "enabled" : "disabled",
                  user.isLocked() ? "locked" : "unlocked", dump(user.getRoles()),
                  dump(getOldPasswordHashes(user))};
          if (create) {
            params[0] = user.getAccountId();
            queryRunner.update(conn, sql, params);
          } else {
            System.arraycopy(params, 1, params, 0, params.length - 1);
            params[params.length - 1] = user.getAccountId();
            queryRunner.update(conn, sql, params);
          }
        } finally {
          DbUtils.closeQuietly(conn);
        }

      } else {
        throw new AuthenticationCredentialsException("Problem saving user",
            "Skipping save of user " + u.getAccountName());
      }

      logger.info(Logger.SECURITY_SUCCESS, "User written to database");
    } catch (Exception e) {
      logger.fatal(Logger.SECURITY_FAILURE, "Problem saving user to database", e);
      throw new AuthenticationException("Internal Error", "Problem saving user to database", e);
    }
  }

  /**
   * Dump a collection as a comma-separated list.
   *
   * @param c the collection to convert to a comma separated list
   * @return a comma separated list containing the values in c
   */
  private String dump(Collection<String> c) {
    StringBuilder sb = new StringBuilder();
    c.stream().forEach((s) -> {
      sb.append(s).append(",");
    });
    if (c.size() > 0) {
      return sb.toString().substring(0, sb.length() - 1);
    }
    return "";

  }

  /**
   * {@inheritDoc}
   * <p/>
   * This implementation simply verifies that account names are at least 5 characters long. This
   * helps to defeat a brute force attack, however the real strength comes from the name length and
   * complexity.
   *
   * @param newAccountName
   */
  public void verifyAccountNameStrength(String newAccountName) throws AuthenticationException {
    if (newAccountName == null) {
      throw new AuthenticationCredentialsException("Invalid account name",
          "Attempt to create account with a null account name");
    }
    if (!ESAPI.validator().isValidInput("verifyAccountNameStrength", newAccountName, "AccountName",
        MAX_ACCOUNT_NAME_LENGTH, false)) {
      throw new AuthenticationCredentialsException("Invalid account name",
          "New account name is not valid: " + newAccountName);
    }
  }

  /**
   * {@inheritDoc}
   * <p/>
   * This implementation checks: - for any 3 character substrings of the old password - for use of a
   * length * character sets > 16 (where character sets are upper, lower, digit, and special jtm -
   * 11/16/2010 - added check to verify pw != username (fix for
   * http://code.google.com/p/owasp-esapi-java/issues/detail?id=108)
   */
  public void verifyPasswordStrength(String oldPassword, String newPassword, User user)
      throws AuthenticationException {
    if (newPassword == null) {
      throw new AuthenticationCredentialsException("Invalid password",
          "New password cannot be null");
    }

    // can't change to a password that contains any 3 character substring of old password
    if (oldPassword != null) {
      int length = oldPassword.length();
      for (int i = 0; i < length - 2; i++) {
        String sub = oldPassword.substring(i, i + 3);
        if (newPassword.indexOf(sub) > -1) {
          throw new AuthenticationCredentialsException("Invalid password",
              "New password cannot contain pieces of old password");
        }
      }
    }

    // new password must have enough character sets and length
    int charsets = 0;
    for (int i = 0; i < newPassword.length(); i++) {
      if (Arrays.binarySearch(EncoderConstants.CHAR_LOWERS, newPassword.charAt(i)) >= 0) {
        charsets++;
        break;
      }
    }
    for (int i = 0; i < newPassword.length(); i++) {
      if (Arrays.binarySearch(EncoderConstants.CHAR_UPPERS, newPassword.charAt(i)) >= 0) {
        charsets++;
        break;
      }
    }
    for (int i = 0; i < newPassword.length(); i++) {
      if (Arrays.binarySearch(EncoderConstants.CHAR_DIGITS, newPassword.charAt(i)) >= 0) {
        charsets++;
        break;
      }
    }
    for (int i = 0; i < newPassword.length(); i++) {
      if (Arrays.binarySearch(EncoderConstants.CHAR_SPECIALS, newPassword.charAt(i)) >= 0) {
        charsets++;
        break;
      }
    }

    // calculate and verify password strength
    int strength = newPassword.length() * charsets;
    if (strength < 16) {
      throw new AuthenticationCredentialsException("Invalid password",
          "New password is not long and complex enough");
    }

    String accountName = user.getAccountName();

    // jtm - 11/3/2010 - fix for bug http://code.google.com/p/owasp-esapi-java/issues/detail?id=108
    if (accountName.equalsIgnoreCase(newPassword)) {
      // password can't be account name
      throw new AuthenticationCredentialsException("Invalid password",
          "Password matches account name, irrespective of case");
    }
  }

}

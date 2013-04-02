package com.dm;

import java.util.Date;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;
import org.owasp.esapi.contrib.spring.authenticator.UserProfile;
import org.owasp.esapi.errors.AuthenticationHostException;

public class User implements UserProfile {

	/** The logger used by the class. */
	private transient final Logger logger = ESAPI.getLogger("User");

	/** This user's account id. */
	long accountId = 0;

	/** This user's account name. */
	private String accountName = "";
	
	/** This user's screen name (account name alias). */
	private String screenName = "";
	
	/** Whether this user's account is locked. */
	private boolean locked = false;
	
	/** Whether this user is logged in. */
	private boolean loggedIn = true;

	/** Whether this user's account is enabled. */
	private boolean enabled = false;

	/** The expiration date/time for this user's account. */
	private Date expirationDate = new Date(Long.MAX_VALUE);
	
	/** The failed login count for this user's account. */
	private int failedLoginCount = 0;
	
    /** The last host address used by this user. */
    private String lastHostAddress;
    
	/** The last password change time for this user. */
	private Date lastPasswordChangeTime = new Date(0);
    
	/** The last login time for this user. */
	private Date lastLoginTime = new Date(0);
    
	/** The last failed login time for this user. */
	private Date lastFailedLoginTime = new Date(0);
	
	/**
	 * Instantiates a new user.
	 *
	 * @param accountName
	 * 		The name of this user's account.
	 */
	public User(String accountName) {
		this.accountName = accountName.toLowerCase();
		while( true ) {
			long id = Math.abs( ESAPI.randomizer().getRandomLong() );
			if ( ESAPI.authenticator().getUser( id ) == null && id != 0 ) {
				this.accountId = id;
				break;
			}
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public long getId() {
		return accountId;
	}

	@Override
	public String getAccountName() {
		return accountName;
	}

	@Override
	public Date getExpirationDate() {
		return (Date)expirationDate.clone();
	}

	@Override
	public int getFailedLoginCount() {
		return failedLoginCount;
	}

	@Override
	public String getLastHostAddress() {
		if ( lastHostAddress == null ) {
			return "unknown";
		}
        return lastHostAddress;
	}

	@Override
	public Date getLastFailedLoginTime() {
		return (Date)lastFailedLoginTime.clone();
	}

	@Override
	public Date getLastLoginTime() {
		return (Date)lastLoginTime.clone();
	}

	@Override
	public Date getLastPasswordChangeTime() {
		return (Date)lastPasswordChangeTime.clone();
	}

	@Override
	public String getScreenName() {
		return screenName;
	}

	@Override
	public void setFailedLoginCount(int count) {
		failedLoginCount = count;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean isLocked() {
		return locked;
	}

	@Override
	public boolean isLoggedIn() {
		return loggedIn;
	}

	@Override
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Override
	public void setAccountName(String accountName) {
		String old = getAccountName();
		this.accountName = accountName.toLowerCase();
		if (old != null) {
			if (old.equals("")) {
				old = "[nothing]";
			}
			logger.info(Logger.SECURITY_SUCCESS, "Account name changed from "
					+ old + " to " + getAccountName());
		}
	}

	@Override
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = new Date( expirationDate.getTime() );
		logger.info(Logger.SECURITY_SUCCESS, "Account expiration date set to " + expirationDate + " for " + getAccountName());
	}

	@Override
	public void setScreenName(String screenName) {
		this.screenName = screenName;
		logger.info(Logger.SECURITY_SUCCESS, "ScreenName changed to " + screenName + " for " + getAccountName() );
	}

	@Override
	public void setLastFailedLoginTime(Date lastFailedLoginTime) {
		this.lastFailedLoginTime = lastFailedLoginTime;
		logger.info(Logger.SECURITY_SUCCESS, "Set last failed login time to " + lastFailedLoginTime + " for " + getAccountName() );
	}

	@Override
	public void setLastHostAddress(String remoteHost) {
		if ( lastHostAddress != null && !lastHostAddress.equals(remoteHost)) {
        	// returning remote address not remote hostname to prevent DNS lookup
			throw new AuthenticationHostException("Host change", "User session just jumped from " + lastHostAddress + " to " + remoteHost );
		}
		lastHostAddress = remoteHost;
	}

	@Override
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
		logger.info(Logger.SECURITY_SUCCESS, "Set last successful login time to " + lastLoginTime + " for " + getAccountName() );
	}

	@Override
	public void setLastPasswordChangeTime(Date lastPasswordChangeTime) {
		this.lastPasswordChangeTime = lastPasswordChangeTime;
		logger.info(Logger.SECURITY_SUCCESS, "Set last password change time to " + lastPasswordChangeTime + " for " + getAccountName() );
	}

	@Override
	public String getFirstName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLastName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSuperUser() {
		// TODO Auto-generated method stub
		return false;
	}

}

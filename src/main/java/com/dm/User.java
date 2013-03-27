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

	/** Whether this user's account is enabled. */
	private boolean enabled = false;

	/** The expiration date/time for this user's account. */
	private Date expirationDate = new Date(Long.MAX_VALUE);
	
	/** The failed login count for this user's account. */
	private int failedLoginCount = 0;
	
    /** The last host address used by this user. */
    private String lastHostAddress;
    
	/** The last failed login time for this user. */
	private Date lastFailedLoginTime = new Date(0);

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getLastPasswordChangeTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScreenName() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setLocked(boolean b) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void setLastPasswordChangeTime(Date lastPasswordChangeTime) {
		// TODO Auto-generated method stub

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

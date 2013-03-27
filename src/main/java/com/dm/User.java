package com.dm;

import java.util.Date;

import org.owasp.esapi.contrib.spring.authenticator.UserProfile;

public class User implements UserProfile {

	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getAccountName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getExpirationDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFailedLoginCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLastHostAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getLastFailedLoginTime() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub

	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void setExpirationDate(Date expirationTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setScreenName(String screenName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLastFailedLoginTime(Date lastFailedLoginTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLastHostAddress(String remoteHost) {
		// TODO Auto-generated method stub

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

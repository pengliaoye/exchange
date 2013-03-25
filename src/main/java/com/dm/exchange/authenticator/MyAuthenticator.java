package com.dm.exchange.authenticator;

import java.util.List;
import java.util.Set;

import org.owasp.esapi.User;
import org.owasp.esapi.contrib.spring.authenticator.SpringSecurityAuthenticatorAdaptor;
import org.owasp.esapi.errors.AuthenticationException;
import org.owasp.esapi.errors.EncryptionException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyAuthenticator extends SpringSecurityAuthenticatorAdaptor<String> {

	@Override
	public String generateStrongPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateStrongPassword(User user, String oldPassword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUser(long accountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUser(String accountName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getUserNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String hashPassword(String password, String accountName)
			throws EncryptionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void verifyAccountNameStrength(String accountName)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyPasswordStrength(String oldPassword, String newPassword,
			User user) throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Authentication getAuthentication(String user,
			UserDetails userDetails, List<GrantedAuthority> authorities) {
		// TODO Auto-generated method stub
		return null;
	}

}

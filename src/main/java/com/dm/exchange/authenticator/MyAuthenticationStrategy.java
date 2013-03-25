package com.dm.exchange.authenticator;

import org.owasp.esapi.User;
import org.owasp.esapi.contrib.spring.authenticator.AuthenticationStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class MyAuthenticationStrategy implements AuthenticationStrategy<String> {

	@Override
	public String authenticate(Authentication authentication)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createUser(String username, String password) {
		// TODO Auto-generated method stub
		return null;
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

}

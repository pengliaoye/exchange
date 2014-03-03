package com.dm.exchange.authenticator;

import java.util.List;

import org.owasp.esapi.contrib.spring.authenticator.AuthoritiesPopulator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class MyAuthoritiesPopulator implements AuthoritiesPopulator {

	@Override
	public List<GrantedAuthority> getAuthoritiesForUser(
			Authentication authentication) {
		// TODO Auto-generated method stub
		return null;
	}

}

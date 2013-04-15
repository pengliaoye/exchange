package com.dm.exchange.authenticator;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.owasp.esapi.Authenticator;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.EncoderConstants;
import org.owasp.esapi.User;
import org.owasp.esapi.contrib.spring.authenticator.SpringSecurityAuthenticatorAdaptor;
import org.owasp.esapi.errors.AuthenticationCredentialsException;
import org.owasp.esapi.errors.AuthenticationException;
import org.owasp.esapi.errors.EncryptionException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyAuthenticator extends SpringSecurityAuthenticatorAdaptor<String> {
	
	private static final int MAX_ACCOUNT_NAME_LENGTH = 250;
	
	private static Authenticator INSTANCE;

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
	public void verifyAccountNameStrength(String newAccountName)
			throws AuthenticationException {
        if (newAccountName == null) {
            throw new AuthenticationCredentialsException("Invalid account name", "Attempt to create account with a null account name");
        }
        if (!ESAPI.validator().isValidInput("verifyAccountNameStrength", newAccountName, "AccountName", MAX_ACCOUNT_NAME_LENGTH, false)) {
            throw new AuthenticationCredentialsException("Invalid account name", "New account name is not valid: " + newAccountName);
        }
	}

	@Override
	public void verifyPasswordStrength(String oldPassword, String newPassword,
			User user) throws AuthenticationException {

        if (newPassword == null) {
            throw new AuthenticationCredentialsException("Invalid password", "New password cannot be null");
        }

        // can't change to a password that contains any 3 character substring of old password
        if (oldPassword != null) {
            int length = oldPassword.length();
            for (int i = 0; i < length - 2; i++) {
                String sub = oldPassword.substring(i, i + 3);
                if (newPassword.indexOf(sub) > -1) {
                    throw new AuthenticationCredentialsException("Invalid password", "New password cannot contain pieces of old password");
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
            throw new AuthenticationCredentialsException("Invalid password", "New password is not long and complex enough");
        }
        
        String accountName = user.getAccountName();
        
        //jtm - 11/3/2010 - fix for bug http://code.google.com/p/owasp-esapi-java/issues/detail?id=108
        if (accountName.equalsIgnoreCase(newPassword)) {
        	//password can't be account name
        	throw new AuthenticationCredentialsException("Invalid password", "Password matches account name, irrespective of case");
        }
	}
	
	public static Authenticator getInstance() {
	      return INSTANCE;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		INSTANCE = this;
	}

	@Override
	public Authentication getAuthentication(String user,
			UserDetails userDetails, List<GrantedAuthority> authorities) {
		// TODO Auto-generated method stub
		return null;
	}

}

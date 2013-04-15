package com.dm.exchange;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Validator;
import org.owasp.esapi.errors.AuthenticationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/applicationContext.xml")
public class HelloWorldTest {
	@Test
	public void testIsValidEmail() throws Exception {
		Validator instance = ESAPI.validator();
		Assert.assertTrue(instance.isValidInput("test",
				"jeff.williams@aspectsecurity.com", "Email", 100, false));
		
		try {
			ESAPI.authenticator().createUser("admin", "!Abc123", "!Abc123");
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
				
	}
}

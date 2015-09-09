package com.dm.exchange;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Validator;
import org.owasp.esapi.errors.AuthenticationException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/applicationContext.xml")
public class HelloWorldTest {
	//@Test
	public void testIsValidEmail() throws Exception {
		Validator instance = ESAPI.validator();
		Assert.assertTrue(instance.isValidInput("test",
				"jeff.williams@aspectsecurity.com", "Email", 100, false));
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(new MockHttpSession());
		ESAPI.httpUtilities().setCurrentHTTP(request,
				new MockHttpServletResponse());
		try {
			ESAPI.authenticator().createUser("admin", "!Abc123", "!Abc123");
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}

	}
	
	//@Test
	public void testArrayCopy(){		
		boolean create = false;
		Object[] params = new Object[] { null, 1, 2 , 3};
		System.out.println(Arrays.toString(params));
    	if(create){
            params[0] = 5;    		
    	} else {
            System.arraycopy(params, 1, params, 0, params.length - 1);
            params[params.length - 1] = 5;    		
    	}    
    	System.out.println(Arrays.toString(params));
	}
}

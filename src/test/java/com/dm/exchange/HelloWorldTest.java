package com.dm.exchange;

import org.junit.Assert;
import org.junit.Test;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Validator;

public class HelloWorldTest
{
    @Test
    public void testIsValidEmail()
    {
        Validator instance = ESAPI.validator();
        Assert.assertTrue(instance.isValidInput("test",
                "jeff.williams@aspectsecurity.com", "Email", 100, false));
    }
}

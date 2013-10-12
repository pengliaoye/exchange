package com.dm.exchange.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class Hello {
	
	private String message = new String("Hello, ");

    public  Hello() {
    }

    @WebMethod
    public String sayHello(String name) {
        return message + name + ".";
    }

}

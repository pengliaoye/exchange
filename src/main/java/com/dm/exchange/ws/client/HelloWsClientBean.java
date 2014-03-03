package com.dm.exchange.ws.client;

import javax.xml.ws.WebServiceRef;

import javax.enterprise.inject.Model;
/*
 *	wsimport -p com.dm.exchange.ws.client -keep HelloService.wsdl
 */
@Model
public class HelloWsClientBean {

	@WebServiceRef(wsdlLocation = "http://localhost:8080/exchange/HelloService?WSDL")
	private HelloService service;

	public void sayHello() {
            Hello port = service.getHelloPort();
            String str = port.sayHello("world");
            System.out.println(str);
	}
}

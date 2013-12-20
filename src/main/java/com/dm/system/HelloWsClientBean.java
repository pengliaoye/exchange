package com.dm.system;

import javax.xml.ws.WebServiceRef;

import com.dm.exchange.ws.client.Hello;
import com.dm.exchange.ws.client.HelloService;
/*
 *	wsimport -p com.dm.exchange.ws.client -keep HelloService.wsdl
 */

public class HelloWsClientBean {

	@WebServiceRef(wsdlLocation = "META-INF/wsdl/HelloService.wsdl")
	private static HelloService service;

	private String sayHello() {
		Hello port = service.getHelloPort();
		return port.sayHello("world");
	}

}

package com.dm.exchange.rest;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

@Model
public class HelloClientBean {
	
	Client client;
	WebTarget target;
	
	@PostConstruct
	public void init(){
		client = ClientBuilder.newClient();
		target = client.target("http://localhost:8080/exchange/webapi/helloworld");
	}
	
	public void destroy(){
		client.close();
	}
	
	public void getHello(){
		String str = target.request().get(String.class);
		System.out.println(str);
	}

}

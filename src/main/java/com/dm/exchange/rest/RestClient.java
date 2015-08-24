package com.dm.exchange.rest;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.Model;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import com.dm.exchange.rest.bean.RecaptchaVerifyReq;
import com.dm.exchange.rest.bean.RecaptchaVerifyResp;

@Model
public class RestClient {

	private static final String G_RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

	Client client;

	@PostConstruct
	public void init() {
		client = ClientBuilder.newClient();
	}

	public RecaptchaVerifyResp recaptchaVerify(String secret, String response) {
		WebTarget target = client.target(G_RECAPTCHA_VERIFY_URL);
		RecaptchaVerifyReq req = new RecaptchaVerifyReq(secret, response);
		RecaptchaVerifyResp resp = target.request().post(Entity.json(req), RecaptchaVerifyResp.class);
		return resp;
	}

	public void getHello() {
		WebTarget target = client.target("http://localhost:8080/exchange/webapi/helloworld");
		String str = target.request().get(String.class);
		System.out.println(str);
	}

	@PreDestroy
	public void destroy() {
		client.close();
	}

}

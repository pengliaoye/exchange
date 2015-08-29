package com.dm.exchange.rest;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.Model;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import com.dm.exchange.rest.bean.RecaptchaVerifyResp;
import java.util.logging.Logger;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import org.glassfish.jersey.filter.LoggingFilter;

@Model
public class RestClient {
    
    private static final Logger logger = Logger.getLogger(RestClient.class.getName());

	private static final String G_RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

	Client client;

	@PostConstruct
	public void init() {
		client = ClientBuilder.newClient();
                client.register(new LoggingFilter(logger, true));
	}

	public RecaptchaVerifyResp recaptchaVerify(String secret, String response) {
		WebTarget target = client.target(G_RECAPTCHA_VERIFY_URL);
                MultivaluedMap<String, String> multiValMap = new MultivaluedHashMap<>();
                multiValMap.add("secret", secret);
                multiValMap.add("response", response);
                Form form = new Form(multiValMap);
		RecaptchaVerifyResp resp = target.request().post(Entity.form(form), RecaptchaVerifyResp.class);
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

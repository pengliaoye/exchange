package com.dm.exchange.rest.bean;

public class RecaptchaVerifyReq {

	private String secret;
	private String response;

	public RecaptchaVerifyReq(String secret, String response) {
		super();
		this.secret = secret;
		this.response = response;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}

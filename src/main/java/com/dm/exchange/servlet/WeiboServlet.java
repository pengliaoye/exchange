package com.dm.exchange.servlet;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthClientResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

@WebServlet(urlPatterns = "/oauth/oauth2/redirect")
public class WeiboServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(WeiboServlet.class
			.getName());

	private Client client;

	@Override
	public void init() throws ServletException {
		client = ClientBuilder.newClient();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			OAuthClientResponse oaresp = OAuthAuthzResponse
					.oauthCodeAuthzResponse(req);
			String responseCode = oaresp.getParam("code");

			OAuthClientRequest request = OAuthClientRequest
					.tokenLocation("https://api.weibo.com/oauth2/access_token")
					.setClientId("237505324")
					.setClientSecret("95b62596e5f9378aa71d97a848da5d19")
					.setGrantType(GrantType.AUTHORIZATION_CODE)
					.setRedirectURI(
							"http://122.73.14.11:8080/exchange/oauth/oauth2/redirect")
					.setCode(responseCode).buildQueryMessage();

			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
			OAuthJSONAccessTokenResponse response = oAuthClient
					.accessToken(request);
			String accessToken = response.getAccessToken();

			String uid = getUid(accessToken);

			WebTarget target = client
					.target("https://api.weibo.com/2/users/show.json");
			String r = target.queryParam("uid", uid)
					.queryParam("access_token", accessToken).request()
					.get(String.class);
			JsonObject jso;
			try (JsonReader reader = Json.createReader(new StringReader(r))) {
				jso = reader.readObject();
			}
			System.out.println(jso.get("screen_name"));
		} catch (OAuthProblemException | OAuthSystemException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public String getUid(String accessToken) throws OAuthSystemException,
			OAuthProblemException {
		WebTarget target = client
				.target("https://api.weibo.com/2/account/get_uid.json");
		String r = target.request()
				.header("Authorization", "OAuth2 " + accessToken)
				.get(String.class);
		JsonReader reader = Json.createReader(new StringReader(r));
		JsonObject jso = reader.readObject();
		String uid = jso.get("uid").toString();
		return uid;
	}

}

package com.dm.exchange.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

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
public class OauthServlet extends HttpServlet {

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
					.setRedirectURI("http://localhost:8080")
					.setCode(responseCode).buildQueryMessage();

			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
			OAuthJSONAccessTokenResponse response = oAuthClient
					.accessToken(request);
			String accessToken = response.getAccessToken();

			String uid = getUid(accessToken);

			Client client = ClientBuilder.newClient();
			WebTarget target = client
					.target("https://api.weibo.com/2/users/show.json");
			Map<String, Object> r = target.queryParam("uid", uid)
					.queryParam("access_token", accessToken).request()
					.get(new GenericType<Map<String, Object>>() {
					});

			System.out.println(r.get("screen_name"));
		} catch (OAuthProblemException | OAuthSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getUid(String accessToken) {
		String uid = null;
		try {
			URL url = new URL("https://api.weibo.com/2/account/get_uid.json");
			URLConnection c = url.openConnection();
			c.addRequestProperty("Authorization", "OAuth2 " + accessToken);
			InputStream inputStream = c.getInputStream();
			JsonParser parser = Json.createParser(inputStream);
			while (parser.hasNext()) {
				switch (parser.next()) {
				case KEY_NAME:
					String key = parser.getString();
					parser.next();
					switch (key) {
					case "uid":
						uid = parser.getString();
						break;

					default:
						break;
					}
					break;

				default:
					break;
				}
			}
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uid;
	}

}

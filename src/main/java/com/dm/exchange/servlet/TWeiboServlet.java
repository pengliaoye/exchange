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
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthClientResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

@WebServlet(urlPatterns = "/oauth2/redirect/tweibo")
public class TWeiboServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(TWeiboServlet.class
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
                    .tokenLocation("https://open.t.qq.com/cgi-bin/oauth2/access_token")
                    .setClientId("1101120886")
                    .setClientSecret("SW9ssjaMvLGHfonv")
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setRedirectURI(
                            "http://122.73.14.11:8080/exchange/oauth2/redirect/tweibo")
                    .setCode(responseCode).buildQueryMessage();

            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            OAuthAccessTokenResponse response = oAuthClient
                    .accessToken(request, GitHubTokenResponse.class);
            String accessToken = response.getAccessToken();

            WebTarget target = client
                    .target("https://open.t.qq.com/api/user/info");
            String r = target.queryParam("access_token", accessToken).request()
                    .get(String.class);
            JsonObject jso;
            try (JsonReader reader = Json.createReader(new StringReader(r))) {
                jso = reader.readObject();
            }
            System.out.println(jso.get("name"));
        } catch (OAuthProblemException | OAuthSystemException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dm.exchange.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author cmp
 */
@WebServlet(name = "QQOpen", urlPatterns = {"/qopen"})
public class QQOpen extends HttpServlet {

    Client client;

    @Override
    public void init() throws ServletException {
        client = ClientBuilder.newClient();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String openid = req.getParameter("openid");
            String openkey = req.getParameter("openkey");
            String pf = req.getParameter("pf");

            String uri = "/v3/user/get_info";
            String enuri = URLEncoder.encode(uri, "UTF-8");

            String param = "appid=1101120886&openid=" + openid + "&openkey=" + openkey + "&pf=" + pf;
            String enparam = URLEncoder.encode(param, "UTF-8");

            String str = "GET" + "&" + enuri + "&" + enparam;

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec("SW9ssjaMvLGHfonv&".getBytes(), "HmacSHA1"));
            byte[] rawHmac = mac.doFinal(str.getBytes());
            String sig = DatatypeConverter.printBase64Binary(rawHmac);

            WebTarget target = client.target("http://119.147.19.43/v3/user/get_info")
                    .queryParam("openid", openid)
                    .queryParam("openkey", openkey)
                    .queryParam("pf", pf)
                    .queryParam("appid", "1101120886")
                    .queryParam("sig", sig);
            String r = target.request().get(String.class);
            System.out.println(r);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            Logger.getLogger(QQOpen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

}

package com.dm.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;

@Dependent
public class HttpService {

	private static final Logger logger = Logger.getLogger(HttpService.class
			.getName());

	private static final int HTTP_TIMEOUT = 6000;

	private static final int HTTP_READTIMEOUT = 6000;

	public static final String CHAR_CODE = "utf-8";

	public String getHttpResp(String url, Map<String, String> headers) {
		BufferedReader reader = null;
		try {
			URL httpUrl = new URL(url);
			URLConnection conn = httpUrl.openConnection();
			conn.setConnectTimeout(HTTP_TIMEOUT);
			conn.setReadTimeout(HTTP_READTIMEOUT);
			Set<Entry<String, String>> set = headers.entrySet();
			for (Entry<String, String> entry : set) {
				conn.addRequestProperty(entry.getKey(), entry.getValue());
			}
			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), CHAR_CODE));
			StringBuilder builder = new StringBuilder();
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				builder.append(temp);
			}
			reader.close();
			return builder.toString();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}
	
	

}

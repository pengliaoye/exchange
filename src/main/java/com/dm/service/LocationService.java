package com.dm.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

@Dependent
public class LocationService {

	private static final Logger logger = Logger.getLogger(LocationService.class
			.getName());

	public static final String PROVINCE_URL = "http://www.ems.com.cn/partner/api/public/p/area/cn/province/list";
	public static final String CITY_URL = "http://www.ems.com.cn/partner/api/public/p/area/cn/city/list";
	public static final String COUNTRY_URL = "http://www.ems.com.cn/partner/api/public/p/area/cn/county/list";

	@Inject
	private HttpService httpService;

	public Map<String, String> getProvinceData() {
		Map<String, String> areas = getEmsResp(PROVINCE_URL);
		return areas;
	}

	public Map<String, String> getCityData(String province) {
		Map<String, String> areas = getEmsResp(CITY_URL+"/"+province);
		return areas;
	}

	public Map<String, String> getCountryData(String city) {
		Map<String, String> areas = getEmsResp(COUNTRY_URL+"/"+city);
		return areas;
	}

	private Map<String, String> getEmsResp(String url) {
		Map<String, String> result = new HashMap<String, String>();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("version", "international_eub_us_1.1");
		headers.put("authenticate", "emseub_15bbd18f29bd3696a2aad23bcb857829");
		String respStr = httpService.getHttpResp(url, headers);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			AreaHandler handler = new AreaHandler();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(new StringReader(respStr)));
			result = handler.getAreas();
		} catch (IOException | ParserConfigurationException | SAXException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return result;
	}

}

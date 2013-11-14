package com.dm.service;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AreaHandler extends DefaultHandler {

	private Map<String, String> areas = new HashMap<String, String>();

	String id = null;

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if ("area".equals(localName)) {
			id = attributes.getValue("id");
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String val = new String(ch, start, length);
		if (id != null) {
			areas.put(id, val);
		}
	}

	public Map<String, String> getAreas() {
		return areas;
	}

}

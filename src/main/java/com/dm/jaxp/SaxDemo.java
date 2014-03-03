package com.dm.jaxp;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class SaxDemo extends DefaultHandler{
	
	// http://www.javacodegeeks.com/2013/05/parsing-xml-using-dom-sax-and-stax-parser-in-java.html
	// http://blog.yamanyar.com/2009/04/differences-between-dom-sax-or-stax.html

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		SAXParserFactory spf = SAXParserFactory.newInstance();		
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		SaxDemo handler = new SaxDemo();
		xmlReader.setContentHandler(handler);				
		URL url = SaxDemo.class.getResource("rich_iii.xml");		
		xmlReader.parse(url.toString());
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String str = new String(ch, start, length);
		System.out.println(str);
	}   
	
	
}

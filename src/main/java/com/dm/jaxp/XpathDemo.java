package com.dm.jaxp;

import java.net.URL;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XpathDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String xpathExpression = "//TITLE";
		String xmlFile = "rich_iii.xml";
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();		
        // SAX as data model
        URL url = XpathDemo.class.getResource(xmlFile);             
        NodeList saxNodeList = null;
        try {
            saxNodeList = (NodeList)xpath.evaluate(xpathExpression,
                    new InputSource(url.toString()),
                    XPathConstants.NODESET);
        } catch (XPathExpressionException xpathExpressionException) {
            xpathExpressionException.printStackTrace();
            System.exit(1);
        };
        dumpNode("SAX via InputSource", xmlFile, xpathExpression, saxNodeList);
	}
	
    static void dumpNode(String objectModel,
            String inputFile,
            String xpathExpression,
            NodeList nodeList) {
        
        System.out.println("Object model: " + objectModel + "created from: " + inputFile + "\n"
                + "XPath expression: " + xpathExpression + "\n"
                + "NodeList.getLength(): " + nodeList.getLength());
        
        // dump each Node's info
        for (int onNode = 0; onNode < nodeList.getLength(); onNode++) {
            
            Node node = nodeList.item(onNode);
            String nodeName = node.getNodeName();
            String nodeValue = node.getTextContent();
            if (nodeValue == null) {
                nodeValue = "null";
            }
            String namespaceURI = node.getNamespaceURI();
            if (namespaceURI == null) {
                namespaceURI = "null";
            }
            String namespacePrefix = node.getPrefix();
            if (namespacePrefix == null) {
                namespacePrefix = "null";
            }
            String localName = node.getLocalName();
            if (localName == null) {
                localName = "null";
            }
            
            System.out.println("result #: " + onNode + "\n"
                    + "\tNode name: " + nodeName + "\n"
                    + "\tNode value: " + nodeValue + "\n"
                    + "\tNamespace URI: " + namespaceURI + "\n"
                    + "\tNamespace prefix: " + namespacePrefix + "\n"
                    + "\tLocal name: " + localName);
        }
        // dump each Node's info
    }

}

package com.hanoseok.utils.auxiliary.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLParser {

	private XPath xPath;
	
	private Document document;
	
	private Node rootNode;

	private String charset = "UTF-8";

	public XMLParser(InputStream streamBody, String charset, String rootName) throws UnsupportedEncodingException, ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		this.charset = charset;
		createDocument(new BufferedReader(new InputStreamReader(streamBody, this.charset)), rootName);
	}
	
	public XMLParser(InputStream streamBody, String rootName) throws UnsupportedEncodingException, ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		createDocument(new BufferedReader(new InputStreamReader(streamBody, this.charset)), rootName);
	}

	public XMLParser(String xml, String rootName) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		createDocument(new StringReader(xml), rootName);
	}

	private void createDocument(Reader reader, String rootName) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		InputSource source = new InputSource(reader);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		document = builder.parse(source);

		XPathFactory xPathFactory = XPathFactory.newInstance();
		xPath = xPathFactory.newXPath();
		
		XPathExpression expression = xPath.compile(rootName);
		this.rootNode = (Node) expression.evaluate(document, XPathConstants.NODE);
	}
	
	public Node getRootNode() throws XPathExpressionException{
		return this.rootNode;
	}

	public List<String> getXMLValueFromRoot(String name) throws XPathExpressionException {
		XPathExpression expression = xPath.compile(name);
		NodeList nodeList = (NodeList) expression.evaluate(rootNode, XPathConstants.NODESET);
		
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < nodeList.getLength(); i++){
			list.add(nodeList.item(i).getChildNodes().item(0).getNodeValue());
		}
		return list;
	}
	
	public List<String> getXMLValueFromNode(Node node, String name) throws XPathExpressionException {
		XPathExpression expression = xPath.compile(name);
		NodeList nodeList = (NodeList) expression.evaluate(node, XPathConstants.NODESET);
		
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < nodeList.getLength(); i++){
			list.add(nodeList.item(i).getChildNodes().item(0).getNodeValue());
		}
		return list;
	}

	public NodeList getNodeListFromRoot(String name) throws XPathExpressionException {
		XPathExpression expression = xPath.compile(name);
		return (NodeList) expression.evaluate(rootNode, XPathConstants.NODESET);
	}

	public NodeList getNodeListFromNode(Node node, String name) throws XPathExpressionException {
		XPathExpression expression = xPath.compile(name);
		return (NodeList) expression.evaluate(node, XPathConstants.NODESET);
	}

	public Node getNodeFromRoot(String name) throws XPathExpressionException {
		XPathExpression expression = xPath.compile(name);
		return  (Node) expression.evaluate(rootNode, XPathConstants.NODE);
	}

	public Node getNodeFromNode(Node node, String name) throws XPathExpressionException {
		XPathExpression expression = xPath.compile(name);
		return (Node) expression.evaluate(node, XPathConstants.NODE);
	}

	public Document getDocument() {
		return this.document;
	}
	
	static String a = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><result><r s=\"1\">4013</r><ret>403</ret><ret>401</ret></result>";
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		XMLParser parser = new XMLParser(a, "result");
		System.out.println(parser.getXMLValueFromNode(parser.getRootNode(), "r").get(0));
		System.out.println(parser.getNodeListFromRoot("ret").item(0).getChildNodes().item(0).getNodeValue());
	}

}

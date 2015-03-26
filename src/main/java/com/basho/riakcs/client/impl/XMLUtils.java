/*
 * Copyright (c) 2012 Basho Technologies, Inc. All Rights Reserved. This file is provided to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.basho.riakcs.client.impl;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.xpath.*;

import org.w3c.dom.*;

public class XMLUtils {
	public static String xpathToContent(String xpathQuery, Object domObject) throws XPathExpressionException {
		Node node = xpathToNode(xpathQuery, domObject);

		if (node != null)
			return node.getTextContent();

		return null;
	}

	public static Node xpathToNode(String xpathQuery, Object domObject) throws XPathExpressionException {
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();

		return (Node) xpath.evaluate(xpathQuery, domObject, XPathConstants.NODE);
	}

	public static List<Node> xpathToNodeList(String xpathQuery, Object domObject) throws XPathExpressionException {
		List<Node> result = new ArrayList<Node>();

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		NodeList nodeList = (NodeList) xpath.evaluate(xpathQuery, domObject, XPathConstants.NODESET);

		for (int i = 0; i < nodeList.getLength(); i++)
			result.add(nodeList.item(i));

		return result;
	}

	public static Document parseToDocument(InputStream is, boolean debugModeEnabled) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(is);

		if (debugModeEnabled)
			System.out.println("Body:\n" + serializeDocument(document) + "\n");

		return document;
	}

	public static String serializeDocument(Document document) throws Exception {
		// Serialize XML document to String.
		StringWriter writer = new StringWriter();
		StreamResult streamResult = new StreamResult(writer);

		DOMSource domSource = new DOMSource(document);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.transform(domSource, streamResult);

		return writer.toString();
	}

}

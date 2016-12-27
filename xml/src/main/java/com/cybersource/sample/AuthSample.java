/* Copyright 2003-2004 CyberSource Corporation */

package com.cybersource.sample;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.cybersource.ws.client.ClientException;
import com.cybersource.ws.client.Utility;
import com.cybersource.ws.client.XMLClient;

/**
 * Sample class that demonstrates how to call Credit Card Authorization using
 * the XML client.
 */
public class AuthSample {

	private static final Logger log = LoggerFactory.getLogger(AuthSample.class);

	/**
	 * Entry point.
	 *
	 * @param args
	 *            command-line arguments. The name of the property file followed
	 *            by the name of the input XML file may be passed as
	 *            command-line arguments. If not passed, it will look for
	 *            "cybs.properties" and "auth.xml", respectively in the current
	 *            directory.
	 */
	public static void main(String[] args) {
		
		// read in properties file.
		Properties props = Utility.readProperties(args);
		log.debug("merchantID: {}", props.getProperty("merchantID"));

		// read in input XML file, replacing _NSURI_ (if any) with the
		// effective namespace URI. See header comment for the method
		// readRequest() for more information.
		Document request = readRequest(props, args);
		if (request == null) {
			return;
		}

		// The sample auth.xml does not have the merchantID element. We will
		// let the XMLClient get the merchantID from props and insert it into
		// the request document.

		try {
			
			displayDocument("CREDIT CARD AUTHORIZATION REQUEST:", request);

			// run transaction now
			Document reply = XMLClient.runTransaction(request, props);

			displayDocument("CREDIT CARD AUTHORIZATION REPLY:", reply);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Reads the input XML file. It replaces "_NSURI_" (if any) with the
	 * effective namespace URI derived from the Properties. The sample file
	 * auth.xml included in the package has this placeholder. This is so that
	 * you would only need to change the properties file in order to test this
	 * sample. In your own application, you would likely have the correct
	 * namespace URI already set in your input XML documents and therefore would
	 * not need to do this.
	 *
	 * @param props
	 *            the Properties object to be used to derive the effective
	 *            namespace URI.
	 * @param commandLineArgs
	 *            the command-line arguments.
	 *
	 * @return Document object.
	 */
	private static Document readRequest(Properties props, String[] commandLineArgs) {
		
		Document doc = null;

		try {
			
			// read in the XML file
			String filename = commandLineArgs.length > 1 ? commandLineArgs[1] : "auth.xml";
			byte[] xmlBytes = Utility.read(filename);

			// replace _NSURI_ (if any) with effective namespace URI.
			String xmlString = new String(xmlBytes, "UTF-8");
			//log.debug("xmlString:\n{}", xmlString);

			int pos = xmlString.indexOf("_NSURI_");
			
			if (pos != -1) {
				
				StringBuilder sb = new StringBuilder(xmlString);
				String namespaceURI = XMLClient.getEffectiveNamespaceURI(props, null);
				//log.debug("found _NSURI_ : {}", namespaceURI);
				sb.replace(pos, pos + 7, namespaceURI);
				xmlBytes = sb.toString().getBytes("UTF-8");
			}

			// load the byte array into a Document object.
			ByteArrayInputStream bais = new ByteArrayInputStream(xmlBytes);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder builder = dbf.newDocumentBuilder();
			doc = builder.parse(bais);
			bais.close();
			
		} catch (ClientException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (doc);
	}

	/**
	 * Displays the content of the Document object.
	 *
	 * @param header
	 *            Header text.
	 * @param doc
	 *            Document object to display.
	 */
	private static void displayDocument(String header, Document doc) {
		log.debug("\n{}", header);

		// Note that Utility.nodeToString() is meant to be used for logging
		// or demo purposes only. As it employs some formatting
		// parameters, parsing the string it returns may not result to a
		// Node object exactly similar to the one passed to it.
		log.debug("\n{}", Utility.nodeToString(doc));
	}

}

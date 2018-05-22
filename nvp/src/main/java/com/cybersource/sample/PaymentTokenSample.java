package com.cybersource.sample;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Properties;

import com.cybersource.ws.client.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sample class that demonstrates how to call api for Payment Tokenization.
 */
public class PaymentTokenSample {

	private static final Logger log = LoggerFactory.getLogger(PaymentTokenSample.class);
	private static Properties props = Utility.readProperties(new String[] { "cybs.properties" });

    public static String paymentToken(String subscriptionID, String merchantReferenceCode) {

    	// read in properties file.
		log.debug("Key file : {}", props.getProperty("keyFilename"));
		log.debug("merchantReferenceCode: {}", merchantReferenceCode);

	    String requestID = null;

	   	HashMap<String, String> request = new HashMap<String, String>();

		request.put( "ccAuthService_run", "true");
		request.put( "ccCaptureService_run", "true");
		//request.put( "invoiceHeader_merchantDescriptor", "TMN Wallet Utility Bill");

		// We will let the Client get the merchantID from props and insert it
		// into the request Map.

		// this is your own tracking number.  CyberSource recommends that you
		// use a unique one for each order.

		request.put( "merchantReferenceCode", merchantReferenceCode);
		request.put( "recurringSubscriptionInfo_subscriptionID", subscriptionID);

		request.put( "purchaseTotals_currency", "THB" );
		request.put( "purchaseTotals_grandTotalAmount", "1234.50" );

		// add more fields here per your business needs

		try {
			displayMap( "CREDIT CARD AUTHORIZATION REQUEST:", request );

			// run transaction now
			Map<String, String>  reply = Client.runTransaction( request, props );

			displayMap( "CREDIT CARD AUTHORIZATION REPLY:", reply );

			// if the authorization was successful, obtain the request id
			// for the follow-on capture later.
			String decision = (String) reply.get( "decision" );
			if ("ACCEPT".equalsIgnoreCase( decision ))
			{
				requestID = (String) reply.get( "requestID" );
			}
		}
		catch (ClientException e) {
			System.out.println( e.getMessage() );
			if (e.isCritical())
			{
				handleCriticalException( e, request );
			}
		}
		catch (FaultException e) {

			System.out.println( e.getMessage() );

			if (e.isCritical()) {
				handleCriticalException( e, request );
			}
		}

		log.debug("requestID : {}", requestID);

		return requestID;

    }

    /*
    public static String createToken(String merchantReferenceCode) {

	   	// read in properties file.
		log.debug("Key file : {}", props.getProperty("keyFilename"));
		log.debug("merchantReferenceCode: {}", merchantReferenceCode);

	    String requestID = null;

	   	HashMap<String, String> request = new HashMap<String, String>();

		request.put( "ccAuthService_run", "true");
		request.put( "invoiceHeader_merchantDescriptor", "TMN Wallet Utility Bill");

		// We will let the Client get the merchantID from props and insert it
		// into the request Map.

		// this is your own tracking number.  CyberSource recommends that you
		// use a unique one for each order.

		request.put( "merchantReferenceCode", merchantReferenceCode);

		request.put( "billTo_firstName", "Krungsri" );
		request.put( "billTo_lastName", "Simple" );
		request.put( "billTo_street1", "1222 Rama 3 Road, Bang Phongphang" );
		request.put( "billTo_city", "Yannawa" );
		request.put( "billTo_state", "Bangkok" );
		request.put( "billTo_postalCode", "10210" );
		request.put( "billTo_country", "TH" );
		request.put( "billTo_email", "customer@mail.com" );
		request.put( "billTo_phoneNumber", "+6622962000" );
		request.put( "billTo_ipAddress", "10.7.7.7" );

		// request.put( "shipTo_firstName", "Jane" );
		// request.put( "shipTo_lastName", "Doe" );
		// request.put( "shipTo_street1", "100 Elm Street" );
		// request.put( "shipTo_city", "San Mateo" );
		// request.put( "shipTo_state", "CA" );
		// request.put( "shipTo_postalCode", "94401" );
		// request.put( "shipTo_country", "US" );

		// request.put( "card_accountNumber", "4012001037141112" );
		request.put( "card_accountNumber", "5200000000000007" );
		request.put( "card_expirationMonth", "12" );
		request.put( "card_expirationYear", "2021" );

		request.put( "purchaseTotals_currency", "THB" );

		// there are two items in this sample
		request.put( "item_0_productName", "KFLTFDIV" );
		request.put( "item_0_productSKU", "SKU00" );
		request.put( "item_0_quantity", "100" );
		request.put( "item_0_unitPrice", "10.00" );

		request.put( "item_1_productName", "KFLTFD70" );
		request.put( "item_1_productSKU", "SKU01" );
		request.put( "item_1_quantity", "100" );
		request.put( "item_1_unitPrice", "5.72" );

		// add more fields here per your business needs

		try {
			displayMap( "CREDIT CARD AUTHORIZATION REQUEST:", request );

			// run transaction now
			Map<String, String>  reply = Client.runTransaction( request, props );

			displayMap( "CREDIT CARD AUTHORIZATION REPLY:", reply );

			// if the authorization was successful, obtain the request id
			// for the follow-on capture later.
			String decision = (String) reply.get( "decision" );
			if ("ACCEPT".equalsIgnoreCase( decision ))
			{
				requestID = (String) reply.get( "requestID" );
			}
		}
		catch (ClientException e) {
			System.out.println( e.getMessage() );
			if (e.isCritical())
			{
				handleCriticalException( e, request );
			}
		}
		catch (FaultException e) {

			System.out.println( e.getMessage() );

			if (e.isCritical()) {
				handleCriticalException( e, request );
			}
		}

		log.debug("requestID : {}", requestID);

		return requestID;
    }

    */

/**
	 * Displays the content of the Map object.
	 *
	 * @param header	Header text.
	 * @param map		Map object to display.
	 */
    private static void displayMap( String header, Map mapraw ) {

	    System.out.println( header );

	    TreeMap<String, String> map = new TreeMap<>(mapraw);
		StringBuffer dest = new StringBuffer();

		if (map != null && !map.isEmpty()) {
			Iterator iter = map.keySet().iterator();
			String key, val;

			while (iter.hasNext()) {
				key = (String) iter.next();
				val = (String) map.get( key );
				dest.append( key + "=" + val + "\n" );
			}
		}

		System.out.println( dest.toString() );
    }


	/**
	 * An exception is considered critical if some type of disconnect occurs
	 * between the client and server and the client can't determine whether the
	 * transaction was successful. If this happens, you might have a
	 * transaction in the CyberSource system that your order system is not
	 * aware of. Because the transaction may have been processed by
	 * CyberSource, you should not resend the transaction, but instead send the
	 * exception information and the order information (customer name, order
	 * number, etc.) to the appropriate personnel at your company to resolve
	 * the problem. They should use the information as search criteria within
	 * the CyberSource Transaction Search Screens to find the transaction and
	 * determine if it was successfully processed. If it was, you should update
	 * your order system with the transaction information. Note that this is
	 * only a recommendation; it may not apply to your business model.
	 *
	 * @param e			Critical ClientException object.
	 * @param request	Request that was sent.
	 */
	private static void handleCriticalException(ClientException e, Map request) {
		// send the exception and order information to the appropriate
		// personnel at your company using any suitable method, e.g. e-mail,
		// multicast log, etc.
	}

	/**
	 * See header comment in the other version of handleCriticalException
	 * above.
	 *
	 * @param e			Critical ClientException object.
	 * @param request	Request that was sent.
	 */
	private static void handleCriticalException(FaultException e, Map request) {
		// send the exception and order information to the appropriate
		// personnel at your company using any suitable method, e.g. e-mail,
		// multicast log, etc.
	}

}

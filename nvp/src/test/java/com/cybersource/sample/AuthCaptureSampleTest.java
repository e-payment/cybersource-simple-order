package com.cybersource.sample;

import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthCaptureSampleTest {

	private static final Logger log = LoggerFactory.getLogger(AuthCaptureSampleTest.class);
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

	//@Test
	public void shouldAcceptAuthorize() throws Exception {

		String merchantReferenceCode = df.format(new Date());
		AuthCaptureSample.runAuth(merchantReferenceCode);
	}

	//@Test
	public void shouldAcceptSale() throws Exception {

		String merchantReferenceCode = df.format(new Date());
		AuthCaptureSample.runSale(merchantReferenceCode);
	}

	//@Test
	public void shouldAuthorizeAndReversal() throws Exception {

		String merchantReferenceCode = df.format(new Date());
		AuthCaptureSample.runAuthorizeAndReversal(merchantReferenceCode);
	}

	/// PAYMENT TOKEN ///

	//@Test
	public void shouldConvertTransactionToCustomerProfile() throws Exception {

		String paymentRequestID = "4924423321026068503527";

		String merchantReferenceCode = df.format(new Date());
		AuthCaptureSample.runConvertTransactionToCustomerProfile(paymentRequestID, merchantReferenceCode);

		// decision=ACCEPT
		// reasonCode=100
		// requestID=4924426422606332203011
		// paySubscriptionCreateReply_reasonCode=100
		// * paySubscriptionCreateReply_subscriptionID=4924426422606332203011
	}

	@Test
	public void shouldAcceptPaymentToken() throws Exception {

		String merchantReferenceCode = df.format(new Date());

		// Card: xxxxxxxxxxxx0002, Expiry: 02/2022
		String tokenId = "4924426422606332203011";

		AuthCaptureSample.runPaymentWithToken(merchantReferenceCode, tokenId);
		// decision=ACCEPT
		// reasonCode=100
	}

	// @Test
	public void shouldRejectPaymentTokenCozCardExpire() throws Exception {

		String merchantReferenceCode = df.format(new Date());

		// Card: xxxxxxxxxxxx1111, Expiry: 04/2016
		String tokenId = "4085059778630176195663";
		
		AuthCaptureSample.runPaymentWithToken(merchantReferenceCode, tokenId);
		// decision=REJECT
		// ccAuthReply_reasonCode=202
		/* Decline - Expired card. You might also receive this
		   if the expiration date you provided does not match the date
		   the issuing bank has on file. */
	}

	// @Test
	public void shouldRejectPaymentTokenCozInvalidToken() throws Exception {

		String merchantReferenceCode = df.format(new Date());

		// Invalid Token Id
		String tokenId = "4895555555555555555555";

		AuthCaptureSample.runPaymentWithToken(merchantReferenceCode, tokenId);
		// decision=REJECT
		// reasonCode=102
		// the subscription (4895555555555555555555) could not be found 
	}

}

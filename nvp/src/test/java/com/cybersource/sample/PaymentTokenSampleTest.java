package com.cybersource.sample;

import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentTokenSampleTest {

	private static final Logger log = LoggerFactory.getLogger(PaymentTokenSampleTest.class);
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

	@Test
	public void testPaymentToken() throws Exception {

		String subscriptionID = "5209349185726549503009";
		String merchantReferenceCode = df.format(new Date());

		PaymentTokenSample.paymentToken(subscriptionID, merchantReferenceCode);
	}
}
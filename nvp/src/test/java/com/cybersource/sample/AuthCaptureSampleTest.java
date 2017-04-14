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

	@Test
	public void shouldAuthorize() throws Exception {

		String merchantReferenceCode = df.format(new Date());
		AuthCaptureSample.runAuthorizeAndCapture(merchantReferenceCode);
	}

}

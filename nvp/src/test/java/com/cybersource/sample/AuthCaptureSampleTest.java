package com.cybersource.sample;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthCaptureSampleTest {

	private static final Logger log = LoggerFactory.getLogger(AuthCaptureSampleTest.class);

	@Test
	public void shouldAuthen() throws Exception {
		AuthCaptureSample.main(new String[] { "cybs.properties"});
	}

}

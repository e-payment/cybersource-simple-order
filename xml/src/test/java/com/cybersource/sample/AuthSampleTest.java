package com.cybersource.sample;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthSampleTest {

	private static final Logger log = LoggerFactory.getLogger(AuthSampleTest.class);

	@Test
	public void shouldAuthen() throws Exception {

		String cfgPath = new File("./cybs.properties").getCanonicalPath();
		String authPath = new File("./auth.xml").getCanonicalPath();

		log.info(" cfgPath : {}", cfgPath);
		log.info("authPath : {}", authPath);

		AuthSample.main(new String[] { cfgPath, authPath });
	}

}

/* Copyright 2003-2004 CyberSource Corporation */

package com.cybersource.sample;

import java.io.File;
import java.lang.reflect.Field;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthSampleTest {

	private static final Logger log = LoggerFactory.getLogger(AuthSampleTest.class);

//	static {
//		try {
//			Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
//			log.debug("{}", field);
//			field.setAccessible(true);
//			field.set(null, java.lang.Boolean.FALSE);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
	
	// /Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/security

	@Test
	public void shouldAuthen() throws Exception {

		String cfgPath = new File("./cybs.properties").getCanonicalPath();
		String authPath = new File("./auth.xml").getCanonicalPath();

		log.info(" cfgPath : {}", cfgPath);
		log.info("authPath : {}", authPath);

		AuthSample.main(new String[] { cfgPath, authPath });
	}

}

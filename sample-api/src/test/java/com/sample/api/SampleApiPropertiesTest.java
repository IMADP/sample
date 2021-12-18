package com.sample.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SampleApiPropertiesTest
 * 
 * @author Anthony DePalma
 */
public class SampleApiPropertiesTest extends SampleApiTest {

	@Autowired
	private SampleApiProperties sampleApiProperties;

	@Test
	public void sampleApiProperties() {
		assertNotNull(sampleApiProperties);
		assertNotNull(sampleApiProperties.getJwtSecret());
	}

}

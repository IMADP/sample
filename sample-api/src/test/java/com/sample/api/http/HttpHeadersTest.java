package com.sample.api.http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.sample.api.SampleApiTest;

/**
 * HttpHeadersTest
 *
 * @author Anthony DePalma
 */
public class HttpHeadersTest extends SampleApiTest {

	@Test
	public void of() {
		HttpHeaders httpHeaders = HttpHeaders.of(Map.of("key", "value"));
		assertEquals("value", httpHeaders.getHeader("key"));
	}

}

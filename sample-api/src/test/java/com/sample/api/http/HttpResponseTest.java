package com.sample.api.http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sample.api.SampleApiTest;

/**
 * HttpResponseTest
 *
 * @author Anthony DePalma
 */
public class HttpResponseTest extends SampleApiTest {

	@Test
	public void collectObjects() {
		HttpUri uri = HttpUri.of("www.example.com");
		HttpMethod method = HttpMethod.GET;
		HttpHeaders headers = HttpHeaders.NONE;
		HttpStatus status = HttpStatus.OK;

		HttpResponses responses = new HttpResponses();
		responses.addResponse(HttpResponse.of(uri, method,  headers,  HttpContent.ofJson(Map.of("a","1")), status));
		responses.addResponse(HttpResponse.of(uri, method,  headers,  HttpContent.ofJson(Map.of("b","2")), status));
		responses.addResponse(HttpResponse.of(uri, method,  headers,  HttpContent.ofJson(Map.of("c","3")), status));

		List<LinkedHashMap<String, String>> results = responses.collectObjects(new TypeReference<LinkedHashMap<String, String>>() {});
		assertEquals(3, results.size());
		assertEquals("1", results.get(0).get("a"));
		assertEquals("2", results.get(1).get("b"));
		assertEquals("3", results.get(2).get("c"));
	}

	@Test
	public void collectArrays() {
		HttpUri uri = HttpUri.of("www.example.com");
		HttpMethod method = HttpMethod.GET;
		HttpHeaders headers = HttpHeaders.NONE;
		HttpStatus status = HttpStatus.OK;

		HttpResponses responses = new HttpResponses();
		responses.addResponse(HttpResponse.of(uri, method,  headers,  HttpContent.ofJson(List.of(1)), status));
		responses.addResponse(HttpResponse.of(uri, method,  headers,  HttpContent.ofJson(List.of(2)), status));
		responses.addResponse(HttpResponse.of(uri, method,  headers,  HttpContent.ofJson(List.of(3)), status));

		List<Integer> results = responses.collectArrays(new TypeReference<ArrayList<Integer>>() {});
		assertEquals(3, results.size());
		assertEquals(1, results.get(0));
		assertEquals(2, results.get(1));
		assertEquals(3, results.get(2));
	}

}

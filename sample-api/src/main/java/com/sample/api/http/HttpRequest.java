package com.sample.api.http;


import org.springframework.http.HttpMethod;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * HttpRequest
 *
 * @author Anthony DePalma
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpRequest {

	/**
	 * Creates a get request.
	 *
	 * @param uri
	 * @param headers
	 * @return HttpRequest
	 */
	public static HttpRequest get(HttpUri uri, HttpHeaders headers) {
		return new HttpRequest(uri, HttpMethod.GET, headers, null);
	}

	/**
	 * Creates a post request.
	 *
	 * @param uri
	 * @param headers
	 * @return HttpRequest
	 */
	public static HttpRequest post(HttpUri uri, HttpHeaders headers, HttpContent content) {
		return new HttpRequest(uri, HttpMethod.POST, headers, content);
	}

	/**
	 * Creates a patch request.
	 *
	 * @param uri
	 * @param headers
	 * @return HttpRequest
	 */
	public static HttpRequest patch(HttpUri uri, HttpHeaders headers, HttpContent content) {
		return new HttpRequest(uri, HttpMethod.PATCH, headers, content);
	}

	/**
	 * Creates a put request.
	 *
	 * @param uri
	 * @param headers
	 * @return HttpRequest
	 */
	public static HttpRequest put(HttpUri uri, HttpHeaders headers, HttpContent content) {
		return new HttpRequest(uri, HttpMethod.PUT, headers, content);
	}

	/**
	 * Creates a delete request.
	 *
	 * @param uri
	 * @param headers
	 * @return HttpRequest
	 */
	public static HttpRequest delete(HttpUri uri, HttpHeaders headers) {
		return new HttpRequest(uri, HttpMethod.DELETE, headers, null);
	}

	// uri
	private final HttpUri uri;

	// method
	private final HttpMethod method;

	// headers
	private final HttpHeaders headers;

	// content
	private final HttpContent content;

	@Override
	public String toString() {
		return String.format("%s %s %s %s", method, uri, headers, content);
	}

}
package com.sample.api.http;

import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * HttpHeaders
 *
 * @author Anthony DePalma
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpHeaders {

	/**
	 * Creates headers from a map.
	 *
	 * @param headerMap
	 * @return HttpHeaders
	 */
	public static HttpHeaders of(Map<String, String> headerMap) {
		return new HttpHeaders(headerMap);
	}

	// empty headers
	public final static HttpHeaders NONE = new HttpHeaders(Map.of());

	// the header map
	private final Map<String, String> headerMap;

	/**
	 * Returns a header.
	 *
	 * @param key
	 * @return String
	 */
	public String getHeader(String key) {
		return headerMap.get(key);
	}

	@Override
	public String toString() {
		return headerMap.toString();
	}

}
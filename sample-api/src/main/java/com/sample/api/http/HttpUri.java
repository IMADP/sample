package com.sample.api.http;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.SneakyThrows;

/**
 * HttpUri
 *
 * @author Anthony DePalma
 */
@Getter
public final class HttpUri {

	/**
	 * Creates a uri.
	 *
	 * @param uri
	 * @return HttpUri
	 */
	public static HttpUri of(String uri) {
		return HttpUri.of(uri, Map.of());
	}

	/**
	 * Creates a uri.
	 *
	 * @param host
	 * @param path
	 * @return HttpUri
	 */
	public static HttpUri of(String host, String path) {
		return HttpUri.of(host + path);
	}

	/**
	 * Creates a uri.
	 *
	 * @param host
	 * @param path
	 * @param paramMap
	 * @return HttpUri
	 */
	public static HttpUri of(String host, String path, Map<String, String> paramMap) {
		return HttpUri.of(host + path, paramMap);
	}

	/**
	 * Creates a uri.
	 *
	 * @param uri
	 * @param paramMap
	 * @return HttpUri
	 */
	public static HttpUri of(String uri, Map<String, String> paramMap) {
		return new HttpUri(uri, paramMap);
	}

	// uri
	private final String uri;

	// constructor
	private HttpUri(String uri, Map<String, String> paramMap) {
		this.uri = paramMap.isEmpty() ? uri : paramMap.entrySet().stream().map(e -> encode(e)).collect(Collectors.joining("&", uri + "?", ""));
	}

	@SneakyThrows
	private String encode(Map.Entry<String, String> entry) {
		return entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString());
	}

	@Override
	public String toString() {
		return uri;
	}

}
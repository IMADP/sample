package com.sample.api.http;

import lombok.Getter;

/**
 * HttpException
 *
 * Exception that occurs while invoking an Http connection.
 *
 * @author Anthony DePalma
 */
@Getter
public final class HttpException extends RuntimeException {

	// request
	private final HttpRequest request;

	// response
	private final HttpResponse response;

	// constructor
	public HttpException(HttpRequest request, HttpResponse response, String message) {
		super(message);
		this.request = request;
		this.response = response;
	}

	// constructor
	public HttpException(HttpRequest request, HttpResponse response, Throwable throwable) {
		super(throwable);
		this.request = request;
		this.response = response;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getMessage());
		sb.append(System.getProperty("line.separator"));
		sb.append("Request: " + request);
		sb.append(System.getProperty("line.separator"));
		sb.append("Response: " + response);
		return sb.toString();
	}

}
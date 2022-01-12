package com.sample.api.http;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sample.api.service.JsonMapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * HttpResponse
 *
 * @author Anthony DePalma
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpResponse {

	/**
	 * Creates an HttpResponse.
	 *
	 * @param uri
	 * @param method
	 * @param headers
	 * @param content
	 * @param status
	 * @return HttpResponse
	 */
	public static HttpResponse of(HttpUri uri, HttpMethod method, HttpHeaders headers, HttpContent content, HttpStatus status) {
		return new HttpResponse(uri, method, headers, content, status);
	}

	// uri
	private final HttpUri uri;

	// method
	private final HttpMethod method;

	// headers
	private final HttpHeaders headers;

	// content
	private final HttpContent content;

	// status
	private final HttpStatus status;

	/**
	 * Converts the response to a type.
	 *
	 * @param type
	 * @return V
	 */
	public <V> V toType(Class<V> type) {
		return JsonMapper.fromJson(content.getContent(), type);
	}

	/**
	 * Converts the response to a type.
	 *
	 * @param type
	 * @return V
	 */
	public <V> V toType(TypeReference<V> type) {
		return JsonMapper.fromJson(content.getContent(), type);
	}

	/**
	 * Asserts that the status matches one of the expected or throws an exception.
	 *
	 * The HttpRequest is not required here, but it will help with debugging to have the
	 * request associated with the response that fails this assertion.
	 *
	 * @param request
	 * @param expected
	 * @throws HttpException
	 */
	public void assertStatus(HttpRequest request, HttpStatus... expected) throws HttpException {

		for(HttpStatus expect : expected) {
			if (status == expect) {
				return;
			}
		}

		String message = String.format("Expected status [%s] but found [%s]", expected, this);
		throw new HttpException(request, this, message);
	}

	// status code checks
	public boolean isOk() {
		return status ==  HttpStatus.OK;
	}

	public boolean isFound() {
		return status ==  HttpStatus.FOUND;
	}

	public boolean isNoContent() {
		return status ==  HttpStatus.NO_CONTENT;
	}

	public boolean isUnauthorized() {
		return status ==  HttpStatus.UNAUTHORIZED;
	}

	public boolean isNotFound() {
		return status ==  HttpStatus.NOT_FOUND;
	}

	public boolean isNotAcceptable() {
		return status ==  HttpStatus.NOT_ACCEPTABLE;
	}

	public boolean isConflict() {
		return status ==  HttpStatus.CONFLICT;
	}

	public boolean isUnprocessableEntity() {
		return status ==  HttpStatus.UNPROCESSABLE_ENTITY;
	}

	public boolean isLocked() {
		return status ==  HttpStatus.LOCKED;
	}

	public boolean isTooManyRequests() {
		return status ==  HttpStatus.TOO_MANY_REQUESTS;
	}

	public boolean isInternalServerError() {
		return status ==  HttpStatus.INTERNAL_SERVER_ERROR;
	}

	@Override
	public String toString() {
		return String.format("%s %s (%s) %s %s", method, uri, status, headers, content);
	}

}

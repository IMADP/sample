package com.sample.api.http;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * HttpResponses
 *
 * @author Anthony DePalma
 */
public final class HttpResponses {
	final List<HttpResponse> responses = new ArrayList<>();

	/**
	 * Adds a response.
	 *
	 * @param response
	 */
	public void addResponse(HttpResponse response) {
		responses.add(response);
	}

	/**
	 * Converts the content of each response into an object and collects them in a list.
	 *
	 * @param type
	 * @return List<V>
	 */
	public <V> List<V> collectObjects(TypeReference<V> type) {
		return responses.stream().map(r -> r.toType(type)).collect(Collectors.toList());
	}

	/**
	 * Converts the content of each response into a list of objects and flattens them into a list.
	 *
	 * @param type
	 * @return List<V>
	 */
	public <V> List<V> collectArrays(TypeReference<ArrayList<V>> type) {
		return responses.stream().flatMap(r -> r.toType(type).stream()).collect(Collectors.toList());
	}

}

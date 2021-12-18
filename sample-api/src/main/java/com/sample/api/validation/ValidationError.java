package com.sample.api.validation;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.buf.StringUtils;

import lombok.Getter;

/**
 * ValidationError
 *
 * Encapsulates all information about a validation error.
 *
 * @author Anthony Depalma
 */
@Getter
public final class ValidationError {

	// the error code
	private final String error;

	// the path of the error
	private final List<String> path;

	// the map of additional attributes
	private final Map<String, Object> attributes;

	// constructor
	ValidationError(String error, Deque<String> currentPath, Map<String, Object> attributes) {
		this.error = error;
		this.path = new ArrayList<>(currentPath.size());
		this.attributes = attributes;
		currentPath.descendingIterator().forEachRemaining(p -> this.path.add(p));
	}

	@Override
	public String toString() {
		return StringUtils.join(path, '.') + '.' + error + attributes;
	}

}

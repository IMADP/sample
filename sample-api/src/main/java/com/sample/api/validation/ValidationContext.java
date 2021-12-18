package com.sample.api.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * ValidationContext
 *
 * A context object used to create field and global errors.
 *
 * @author Anthony DePalma
 */
public class ValidationContext {

	// a stack containing the current object path
	private Deque<String> currentPath = new LinkedList<>();

	// the list of validation errors
	private List<ValidationError> errors = new ArrayList<>();

	// constructor
	ValidationContext() {

	}

	/**
	 * Push a new nested path.
	 *
	 * @param path
	 */
	void pushPath(String path) {
		currentPath.push(path);
	}

	/**
	 * Pop the top path.
	 *
	 */
	void popPath() {
		currentPath.pop();
	}

	/**
	 * Adds a field error.
	 *
	 * @param field
	 * @param message
	 */
	public void addFieldError(String field, String error) {
		addFieldError(field, error, Collections.emptyMap());
	}

	/**
	 * Adds a field error with additional attributes.
	 *
	 * @param field
	 * @param error
	 * @param attributes
	 */
	public void addFieldError(String field, String error, Map<String, Object> attributes) {
		pushPath(field);
		errors.add(new ValidationError(error, currentPath, attributes));
		popPath();
	}

	/**
	 * Adds a global error.
	 *
	 * @param message
	 */
	public void addGlobalError(String error) {
		addGlobalError(error, Collections.emptyMap());
	}

	/**
	 * Adds a global error with additional attributes.
	 *
	 * @param error
	 * @param attributes
	 */
	public void addGlobalError(String error, Map<String, Object> attributes) {
		errors.add(new ValidationError(error, currentPath, attributes));
	}

	/**
	 * Returns true if the context has any errors.
	 *
	 * @return boolean
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	/**
	 * Returns the list of errors.
	 *
	 * @return List<ValidationError>
	 */
	public List<ValidationError> getErrors() {
		return errors;
	}

	@Override
	public String toString() {
		return errors.toString();
	}

}

package com.sample.api.validation;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Getter;

/**
 * ValidationException
 *
 * Exception thrown in the event of one or more validation errors.
 *
 * @author Anthony DePalma
 */
@Getter
public final class ValidationException extends RuntimeException {

	/**
	 * Factory method to create an exception from a single global error.
	 * This method should be used for errors that occur outside of a validator.
	 *
	 * @param error
	 * @return ValidationException
	 */
	public static ValidationException of(String error) {
		return ValidationException.of(error, Collections.emptyMap());
	}

	/**
	 * Factory method to create an exception from a single global error.
	 * This method should be used for errors that occur outside of a validator.
	 *
	 * @param error
	 * @param attributes
	 * @return ValidationException
	 */
	public static ValidationException of(String error, Map<String, Object> attributes) {
		ValidationError validationError = new ValidationError(error, new LinkedList<>(), attributes);
		return new ValidationException(Arrays.asList(validationError));
	}

	/**
	 * Factory method to create an exception from a single field error.
	 * This method should be used for errors that occur outside of a validator.
	 *
	 * @param field
	 * @param error
	 * @return ValidationException
	 */
	public static ValidationException of(String field, String error) {
		return ValidationException.of(field, error, Collections.emptyMap());
	}

	/**
	 * Factory method to create an exception from a single field error.
	 * This method should be used for errors that occur outside of a validator.
	 *
	 * @param field
	 * @param error
	 * @param attributes
	 * @return ValidationException
	 */
	public static ValidationException of(String field, String error, Map<String, Object> attributes) {
		ValidationError validationError = new ValidationError(error, new LinkedList<>(Arrays.asList(field)), attributes);
		return new ValidationException(Arrays.asList(validationError));
	}

	// the list of errors
	private final List<ValidationError> errors;

	// constructor
	ValidationException(List<ValidationError> errors) {
		super(errors.toString());
		this.errors = errors;
	}

}

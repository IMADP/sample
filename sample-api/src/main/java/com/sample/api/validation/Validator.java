package com.sample.api.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

/**
 * Validator
 *
 * Extendable validator that defaults to javax/hibernate annotation validation.
 *
 * @param <V>
 * @author Anthony DePalma
 */
@Slf4j
@Component
public class Validator<V> {

	// the set of unused attributes from the javax annotation validator
	private static final Set<String> UNUSED_ATTRIBUTES = Sets.newHashSet("groups", "payload", "message");

	// annotation validator
	private javax.validation.Validator annotationValidator = Validation.buildDefaultValidatorFactory().usingContext().getValidator();

	/**
	 * Validates an object and throws a ValidationException in the event of any validation errors.
	 *
	 * @param object
	 * @throws ValidationException
	 */
	public final void validate(V object) throws ValidationException {
		log.debug("Validating object [{}]", object.getClass().getSimpleName());
		ValidationContext context = new ValidationContext();
		doValidation(object, context);

		if(context.hasErrors()) {
			log.debug("Validation failure for object [{}] [{}]", object.getClass().getSimpleName(), context.getErrors());
			throw new ValidationException(context.getErrors());
		}
	}

	/**
	 * Peforms validation through javax.validation annotations.
	 * 
	 * Subclasses can override this method to perform additional validation or delegate to other validators using <code>validateNested()</code>.
	 * Subclasses should call <code>super.doValidation()</code> when overriding this method if they want to perform annotaiton validation. 
	 *
	 * @param value
	 * @param context
	 */
	protected void doValidation(V object, ValidationContext context) {
		for (ConstraintViolation<V> constraintViolation : annotationValidator.validate(object)) {
			String field = constraintViolation.getPropertyPath().toString();
			String error = constraintViolation.getMessage();
			Map<String, Object> attributes = new HashMap<> (constraintViolation.getConstraintDescriptor().getAttributes());
			attributes.keySet().removeAll(UNUSED_ATTRIBUTES);
			context.addFieldError(field, error, attributes);
		}
	}

	/**
	 * Validates a list of nested objects.
	 * This method is intended to be called from inside the validateAfterAnnotations() method to another validator.
	 *
	 * @param objects
	 * @param path
	 * @param context
	 */
	public void validateNested(List<V> objects, String path, ValidationContext context) {
		for(int i = 0; i < objects.size(); i++) {
			validateNested(objects.get(i), String.valueOf(i), context);
		}
	}

	/**
	 * Validates a nested object.
	 * This method is intended to be called from inside the validateAfterAnnotations() method to another validator.
	 *
	 * @param object
	 * @param path
	 * @param context
	 */
	public final void validateNested(V object, String path, ValidationContext context) {
		context.pushPath(path);
		doValidation(object, context);
		context.popPath();
	}

}

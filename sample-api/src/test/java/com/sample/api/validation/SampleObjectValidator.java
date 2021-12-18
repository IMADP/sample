package com.sample.api.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * SampleObjectValidator
 *
 * @author Anthony DePalma
 */
@Component
public class SampleObjectValidator extends Validator<SampleObject> {

	@Autowired
	private Validator<Object> validator;

	@Override
	protected void doValidation(SampleObject object, ValidationContext errors) {
		super.doValidation(object, errors);
		validator.validateNested(object.getNestedObject(), "nestedObject", errors);
	}

}

package com.sample.api.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sample.api.SampleApiTest;
import com.sample.api.validation.SampleObject.SampleNestedObject;

/**
 * ValidatorTest
 * 
 * @author Anthony DePalma
 */
public class ValidatorTest extends SampleApiTest {

	@Autowired
	private SampleObjectValidator sampleObjectValidator;

	@Test
	public void validationFailure() {
		try
		{
			SampleObject sampleObject = new SampleObject();
			sampleObjectValidator.validate(sampleObject);
		}
		catch(ValidationException exception)
		{
			List<ValidationError> errors = exception.getErrors();
			assertEquals(3, errors.size());

			// range error
			ValidationError one = errors.stream().filter(e -> e.getPath().get(0).equals("size")).findFirst().orElse(null);
			assertTrue(one.getError().equals("range"));
			assertEquals(1, one.getPath().size());
			assertEquals("size", one.getPath().get(0));
			assertEquals(2, one.getAttributes().size());

			// not null error
			ValidationError two = errors.stream().filter(e -> e.getPath().get(0).equals("value")).findFirst().orElse(null);
			assertTrue(two.getError().equals("notNull"));
			assertEquals(1, two.getPath().size());
			assertEquals("value", two.getPath().get(0));
			assertEquals(0, two.getAttributes().size());

			// nested not null error
			ValidationError three = errors.stream().filter(e -> e.getPath().get(0).equals("nestedObject")).findFirst().orElse(null);
			assertTrue(three.getError().equals("notNull"));
			assertEquals(2, three.getPath().size());
			assertEquals("nestedObject", three.getPath().get(0));
			assertEquals("nestedValue", three.getPath().get(1));
			assertEquals(0, three.getAttributes().size());
		}
	}

	@Test
	public void validationSuccess() {
		SampleObject sampleObject = new SampleObject();
		sampleObject.setSize(2);
		sampleObject.setValue("value");

		SampleNestedObject nestedObject = new SampleNestedObject();
		nestedObject.setNestedValue("nestedValue");
		sampleObject.setNestedObject(nestedObject);
	}

}

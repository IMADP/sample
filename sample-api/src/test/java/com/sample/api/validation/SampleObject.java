package com.sample.api.validation;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Data;

/**
 * SampleObject
 *
 * @author Anthony DePalma
 */
@Data
public class SampleObject {

	@Range(min = 5, max = 10)
	private int size;

	@NotNull
	private String value;

	@NotNull
	private SampleNestedObject nestedObject = new SampleNestedObject();

	/**
	 * SampleNestedObject
	 *
	 * @author Anthony DePalma
	 */
	public static class SampleNestedObject {

		@NotNull
		private String nestedValue;

		// getters and setters
		public String getNestedValue() {
			return nestedValue;
		}

		public void setNestedValue(String nestedValue) {
			this.nestedValue = nestedValue;
		}

	}


}

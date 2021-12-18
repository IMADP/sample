package com.sample.api.service.email;

import com.google.common.base.CaseFormat;

/**
 * EmailTemplate
 *
 * @author Anthony DePalma
 */
public enum EmailTemplate {
	RESET_USER,
	VERIFY_USER;

	// the base path for all email templates
	private static final String TEMPLATE_PATH = "email/%s.html";

	/**
	 * Returns the template name in lower camel case.
	 *
	 * @return String
	 */
	public String toLowerCamel() {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
	}

	/**
	 * Returns the template path.
	 *
	 * @return String
	 */
	public String getPath() {
		return String.format(TEMPLATE_PATH, toLowerCamel());
	}

}

package com.sample.api.service.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * UserEmailRequest
 *
 * @author Anthony DePalma
 */
@Data
public class UserEmailRequest {

	@Email
	@NotNull
	private String email;

}
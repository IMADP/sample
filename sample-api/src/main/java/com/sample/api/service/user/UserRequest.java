package com.sample.api.service.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * UserRequest
 *
 * @author Anthony DePalma
 */
@Data
public class UserRequest {

	@Email
	@NotNull
	private String email;

	@NotNull
	private String password;

	// constructor
	public UserRequest() {

	}

	// constructor
	public UserRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}

}
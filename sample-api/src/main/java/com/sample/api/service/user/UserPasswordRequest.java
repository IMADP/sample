package com.sample.api.service.user;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * UserPasswordRequest
 *
 * @author Anthony DePalma
 */
@Data
public class UserPasswordRequest {

	@NotNull
	private String password;

}
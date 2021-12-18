package com.sample.api.service.auth;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * AuthRequest
 *
 * Request object to obtain an authorization token.
 *
 * @author Anthony DePalma
 */
@Data
public class AuthRequest {

	@NotNull
	private String username;

	@NotNull
	private String password;

	private boolean longExpire;

}
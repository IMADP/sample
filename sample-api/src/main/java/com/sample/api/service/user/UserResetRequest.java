package com.sample.api.service.user;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * UserResetRequest
 *
 * @author Anthony DePalma
 */
@Data
public class UserResetRequest {

	@NotNull
	private UUID token;

	@NotNull
	private String password;

}
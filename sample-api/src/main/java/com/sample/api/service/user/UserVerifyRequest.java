package com.sample.api.service.user;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * UserVerifyRequest
 *
 * @author Anthony DePalma
 */
@Data
public class UserVerifyRequest {

	@NotNull
	private UUID token;

}
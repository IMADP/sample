package com.sample.api.service.user;

import com.sample.api.service.AbstractEntityResponse;

import lombok.Getter;
import lombok.Setter;

/**
 * UserResponse
 *
 * @author Anthony DePalma
 */
@Getter
@Setter
public class UserResponse extends AbstractEntityResponse {
	private String email;
	private UserAuthority authority;
	private boolean verified;

	// constructor
	public UserResponse(User user) {
		super(user);
		this.email = user.getEmail();
		this.authority = user.getAuthority();
		this.verified = user.isVerified();
	}

}
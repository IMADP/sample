package com.sample.api.service.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * UserAuthority
 *
 * Enumerated values of user authorities.
 *
 * @author Anthony DePalma
 */
public enum UserAuthority implements GrantedAuthority {
	ROLE_GUEST,
	ROLE_USER,
	ROLE_ADMIN;

	/**
	 * Returns true if this is a guest.
	 *
	 * @return boolean
	 */
	public boolean isGuest() {
		return this == ROLE_GUEST;
	}

	/**
	 * Returns true if this is a user.
	 *
	 * @return boolean
	 */
	public boolean isUser() {
		return this == ROLE_USER;
	}

	/**
	 * Returns true if this is an admin.
	 *
	 * @return boolean
	 */
	public boolean isAdmin() {
		return this == ROLE_ADMIN;
	}

	@Override
	public String getAuthority() {
		return name();
	}

}
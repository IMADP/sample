package com.sample.api.service.auth;

import java.util.Objects;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sample.api.service.user.User;
import com.sample.api.service.user.UserAuthority;

import lombok.Getter;
import lombok.Setter;

/**
 * AuthClaim
 *
 * Encapsulates all the claim details stored inside a jwt.
 * The json property names are kept short as this object is serialized and sent with every request.
 * This object is stored inside the thread local SecurityContext, so it can be retrieved statically from the executing thread.
 *
 * @author Anthony DePalma
 */
@Getter
@Setter
public class AuthClaim {

	// the jwt auth claim key
	public static final String KEY = "clm";

	/**
	 * Returns the current AuthClaim from the SecurityContext.
	 *
	 * @return AuthClaim
	 */
	public static AuthClaim getCurrentClaim() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (AuthClaim) authentication.getPrincipal();
	}

	@JsonProperty("uid")
	private UUID userId;

	@JsonProperty("ath")
	private UserAuthority authority;

	@JsonProperty("pdt")
	private long passwordDate;

	@JsonProperty("lex")
	private boolean longExpiration;

	// constructor
	public AuthClaim() {

	}

	// constructor
	public AuthClaim(User user, boolean longExpiration) {
		this.userId = user.getId();
		this.authority = user.getAuthority();
		this.passwordDate = user.getPasswordDate().getEpochSecond();
		this.longExpiration = longExpiration;
	}

	/**
	 * Returns true if this auth claim matches the values in the User object.
	 * This method will return true if the authority, password date, and id are all identical.
	 *
	 * @param user
	 * @return boolean
	 */
	public boolean isMatch(User user) {

		// if the user's authority doesn't match, return false
		if(!Objects.equals(authority, user.getAuthority())) {
			return false;
		}

		// if the user's password date doesn't match, return false
		if(!Objects.equals(passwordDate, user.getPasswordDate().getEpochSecond())) {
			return false;
		}

		// return true if the user id matches
		return Objects.equals(userId, user.getId());
	}

}
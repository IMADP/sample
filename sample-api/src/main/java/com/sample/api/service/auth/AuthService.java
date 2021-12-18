package com.sample.api.service.auth;

import java.security.Key;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.sample.api.SampleApiProperties;
import com.sample.api.service.user.User;
import com.sample.api.service.user.UserService;
import com.sample.api.validation.Validator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;

/**
 * AuthService
 *
 * Service that handles user authentication and authorization functionality.
 *
 * @author Anthony DePalma
 */
@Component
@RequiredArgsConstructor
public class AuthService {

	// properties
	private final SampleApiProperties properties;
	private final UserService userService;
	private final Validator<Object> validator;

	/**
	 * Returns a token if the request was valid.
	 *
	 * @param request
	 */
	public String getToken(AuthRequest request) {

		// validate the request
		validator.validate(request);

		// authenticate the user
		User user = userService.authenticateUser(request.getUsername(), request.getPassword());

		// return a new token
		return createToken(user, request.isLongExpire());
	}

	/**
	 * Creates a new token containing:
	 *
	 * <code>iat:</code> The time the token was issued
	 * <code>exp:</code> The expiration time, with either a long or short duration
	 * <code>clm:</code> AuthClaim object holding user claim details
	 *
	 * @param user
	 * @param longExpire
	 * @return String
	 */
	private String createToken(User user, boolean longExpire) {
		Instant now = Instant.now();
		Instant expiration = now.plusSeconds(longExpire ? properties.getJwtExpLong() : properties.getJwtExpShort());
		AuthClaim claim = new AuthClaim(user, longExpire);

		return Jwts.builder()
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(expiration))
				.claim(AuthClaim.KEY, claim)
				.signWith(getKey())
				.compact();
	}

	/**
	 * Creates a new token from the given jwt, and optionally resets the issuedAt date.
	 *
	 * @param jws
	 * @param renewIssuedAt
	 * @return String
	 */
	public String renewToken(Jws<Claims> jws, boolean renewIssuedAt) {
		AuthClaim claim = jws.getBody().get(AuthClaim.KEY, AuthClaim.class);
		Instant expiration = Instant.now().plusSeconds(claim.isLongExpiration() ? properties.getJwtExpLong() : properties.getJwtExpShort());
		Date issuedAt = renewIssuedAt ? Date.from(Instant.now()) : jws.getBody().getIssuedAt();

		return Jwts.builder()
				.setIssuedAt(issuedAt)
				.setExpiration(Date.from(expiration))
				.claim(AuthClaim.KEY, claim)
				.signWith(getKey())
				.compact();
	}

	/**
	 * Validates a token by checking the user against the database.
	 * This is not done on every request, but rather during regular short expiration intervals.
	 *
	 * @param jws
	 * @return Optional<Boolean> which will be empty if validation was not performed
	 */
	public Optional<Boolean> validateToken(Jws<Claims> jws) {
		Instant issuedAt = jws.getBody().getIssuedAt().toInstant();
		Instant validateOn = issuedAt.plusSeconds(properties.getJwtExpShort());

		// if the validation deadline is still in the future, return an empty optional
		if(validateOn.isAfter(Instant.now())) {
			return Optional.empty();
		}

		// otherwise ensure that the user in the database is valid
		AuthClaim claim = jws.getBody().get(AuthClaim.KEY, AuthClaim.class);
		User user = userService.getById(claim.getUserId());

		// if the claim doesn't match the user details, return false
		if(!claim.isMatch(user)) {
			return Optional.of(Boolean.FALSE);
		}

		// return true if the user is enabled
		return Optional.of(user.isEnabled());
	}

	/**
	 * Returns an Authentication object from the jwt token.
	 *
	 * @param jws
	 * @return Authentication
	 */
	public Authentication getAuthentication(Jws<Claims> jws) {
		AuthClaim claim = jws.getBody().get(AuthClaim.KEY, AuthClaim.class);
		List<GrantedAuthority> authorities = Arrays.asList(claim.getAuthority());
		Authentication authentication = new UsernamePasswordAuthenticationToken(claim, null, authorities);
		return authentication;
	}

	/**
	 * Parses a token.
	 *
	 * @param token
	 * @throws ExpiredJwtException
	 * @throws UnsupportedJwtException
	 * @throws MalformedJwtException
	 * @throws SignatureException
	 * @throws IllegalArgumentException
	 * @return Jws<Claims>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Jws<Claims> parseToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
		Map<String, Class<AuthClaim>> map = Collections.singletonMap(AuthClaim.KEY, AuthClaim.class);

		return Jwts.parserBuilder()
				.deserializeJsonWith(new JacksonDeserializer(map))
				.setSigningKey(getKey())
				.build()
				.parseClaimsJws(token);
	}

	/**
	 * Creates the HMAC key using the jwt secret.
	 *
	 * @return Key
	 */
	private Key getKey() {
		byte[] secret = Base64.getDecoder().decode(properties.getJwtSecret());
		return Keys.hmacShaKeyFor(secret);
	}

}
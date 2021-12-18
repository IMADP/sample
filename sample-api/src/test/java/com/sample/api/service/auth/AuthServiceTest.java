package com.sample.api.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sample.api.SampleApiTest;
import com.sample.api.service.user.User;
import com.sample.api.service.user.UserAuthority;
import com.sample.api.service.user.UserRequest;
import com.sample.api.service.user.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * AuthServiceTest
 * 
 * @author Anthony DePalma
 */
public class AuthServiceTest extends SampleApiTest {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserService userService;

	@Test
	public void getToken() {

		// create a valid user
		UserRequest userRequest = new UserRequest();
		userRequest.setEmail("email@email.com");
		userRequest.setPassword("password");
		User user = userService.createUser(userRequest, UserAuthority.ROLE_USER);

		// create an auth request
		AuthRequest authRequest = new AuthRequest();
		authRequest.setUsername(userRequest.getEmail());
		authRequest.setPassword(userRequest.getPassword());
		authRequest.setLongExpire(false);

		String token = authService.getToken(authRequest);
		Jws<Claims> jws = authService.parseToken(token);
		AuthClaim claim = jws.getBody().get(AuthClaim.KEY, AuthClaim.class);

		assertEquals(UserAuthority.ROLE_USER, claim.getAuthority());
		assertEquals(user.getId(), claim.getUserId());
		assertEquals(user.getPasswordDate().getEpochSecond(), claim.getPasswordDate());

		Optional<Boolean> valid = authService.validateToken(jws);

		assertTrue(valid.isEmpty());
	}

}

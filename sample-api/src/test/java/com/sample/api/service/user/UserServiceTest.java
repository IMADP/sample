package com.sample.api.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sample.api.SampleApiTest;

/**
 * UserServiceTest
 * 
 * @author Anthony DePalma
 */
public class UserServiceTest extends SampleApiTest {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	public void createUser() {

		// create a valid user
		UserRequest userRequest = new UserRequest();
		userRequest.setEmail("email@email.com");
		userRequest.setPassword("password");
		User user = userService.createUser(userRequest, UserAuthority.ROLE_USER);

		// create an auth request
		assertNotNull(user);
		assertNotNull(user.getId());
		assertNotNull(user.getResetToken());
		assertNotNull(user.getVerifyToken());
		assertNotNull(user.getPasswordDate());
		assertNotNull(user.getTimeCreated());
		assertNotNull(user.getTimeModified());
		assertEquals(0L, user.getVersion());
		assertEquals(UserAuthority.ROLE_USER, user.getAuthority());
		assertEquals(userRequest.getEmail(), user.getEmail());
		assertTrue(passwordEncoder.matches(userRequest.getPassword(), user.getPassword()));
	}

}

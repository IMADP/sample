package com.sample.api.service.user;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sample.api.service.email.Email;
import com.sample.api.service.email.EmailService;
import com.sample.api.service.email.EmailTemplate;
import com.sample.api.validation.ValidationException;
import com.sample.api.validation.Validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * UserService
 *
 * @author Anthony DePalma
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	// properties
	private final EmailService emailService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final Validator<Object> validator;
	private final UserRequestValidator userRequestValidator;

	/**
	 * Gets a user by id.
	 *
	 * @param id
	 * @return User
	 * @throws EntityNotFoundException
	 */
	public User getById(UUID id) throws EntityNotFoundException {
		return userRepository.getById(id);
	}

	/**
	 * Creates a user with the given authority.
	 *
	 * @param request
	 * @param authority
	 * @return User
	 */
	@Transactional
	public User createUser(UserRequest request, UserAuthority authority) {
		log.debug("Creating user [{}]", request.getEmail());

		// validate the request
		userRequestValidator.validate(request);

		// create the user
		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setPasswordDate(Instant.now());
		user.setVerifyToken(UUID.randomUUID());
		user.setResetToken(UUID.randomUUID());
		user.setAuthority(authority);
		user.setEnabled(true);
		user.setVerified(false);
		return saveUser(user);
	}

	/**
	 * Authenticates a user by email and password.
	 * Returns a user if authentication was successful, or throws a ValidationException otherwise.
	 *
	 * @param email
	 * @param password
	 * @return User
	 */
	public User authenticateUser(String email, String password) {

		// look up the user
		Optional<User> optionalUser = userRepository.findByEmail(email);

		// validate the authentication attempt
		if(optionalUser.isEmpty()) {
			throw ValidationException.of("email", "notFound");
		}

		if(!passwordEncoder.matches(password, optionalUser.get().getPassword())) {
			throw ValidationException.of("password", "notPassword");
		}

		if(!optionalUser.get().isEnabled()) {
			throw ValidationException.of("notEnabled");
		}

		return optionalUser.get();
	}

	/**
	 * Sends a verification email to the given user.
	 *
	 * @param userId
	 */
	@Transactional
	public void sendVerifyEmail(UUID userId) {
		log.debug("Sending verify email for user [{}]", userId);
		User user = userRepository.getById(userId);

		if(user.isVerified())
			throw ValidationException.of("verified");

		// update email token
		user.setVerifyToken(UUID.randomUUID());
		user = saveUser(user);

		// send the email
		Map<String, Object> context = Map.of("user", user);
		Email email = emailService.createEmail(EmailTemplate.VERIFY_USER, context);
		email.setToAddress(user.getEmail());
		emailService.sendEmail(email);
	}

	/**
	 * Verifies a user with the given token.
	 *
	 * @param verifyToken
	 * @return user
	 */
	@Transactional
	public User verifyUser(UserVerifyRequest request) {
		log.debug("Verify user");

		// validate the request
		validator.validate(request);

		// verify the user
		User user = userRepository.getByVerifyToken(request.getToken());
		user.setVerifyToken(UUID.randomUUID());
		user.setVerified(true);
		return saveUser(user);
	}

	/**
	 * Updates a user's email address
	 *
	 * @param userId
	 * @param request
	 * @return User
	 */
	@Transactional
	public User updateEmail(UUID userId, UserEmailRequest request) {
		log.debug("Updating email for user [{}]", userId);

		// validate the request
		validator.validate(request);

		// update the user
		User user = userRepository.getById(userId);
		user.setEmail(request.getEmail());
		user.setVerifyToken(UUID.randomUUID());
		user.setVerified(false);
		return saveUser(user);
	}

	/**
	 * Sends a password reset email to the given user.
	 *
	 * @param userId
	 */
	@Transactional
	public void sendResetEmail(UUID userId) {
		log.debug("Sending reset email for user [{}]", userId);
		User user = userRepository.getById(userId);

		// update reset token
		user.setResetToken(UUID.randomUUID());
		user = saveUser(user);

		// send the email
		Map<String, Object> context = Map.of("user", user);
		Email email = emailService.createEmail(EmailTemplate.RESET_USER, context);
		email.setToAddress(user.getEmail());
		emailService.sendEmail(email);
	}

	/**
	 * Resets a user's password.
	 *
	 * @param verifyToken
	 */
	@Transactional
	public User resetUser(UserResetRequest request) {
		log.debug("Reset user");

		// validate the request
		validator.validate(request);

		// reset the password;
		User user = userRepository.getByResetToken(request.getToken());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setPasswordDate(Instant.now());
		user.setResetToken(UUID.randomUUID());
		return saveUser(user);
	}

	/**
	 * Updates a user's password
	 *
	 * @param userId
	 * @param request
	 * @return User
	 */
	@Transactional
	public User updatePassword(UUID userId, UserPasswordRequest request) {
		log.debug("Updating password for user [{}]", userId);

		// validate the request
		validator.validate(request);

		// update the user
		User user = userRepository.getById(userId);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setPasswordDate(Instant.now());
		return saveUser(user);
	}

	/**
	 * Returns a list of all users.
	 *
	 * @return List<User>
	 */
	public List<User> findUsers() {
		log.debug("Finding all users");
		return userRepository.select().fetch();
	}

	/**
	 * Validates and saves a user.
	 *
	 * @param user
	 * @return User
	 */
	private User saveUser(User user) {
		log.debug("Saving user [{}]", user.getId());
		validator.validate(user);
		return userRepository.save(user);
	}

}
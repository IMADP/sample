package com.sample.api.service.user;

import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.JPQLQuery;
import com.sample.api.service.EntityRepository;

/**
 * UserRepository
 *
 * Provides common retrieval operations for entities.
 *
 * @author Anthony DePalma
 */
@Repository
interface UserRepository extends EntityRepository<User> {

	// logger
	final Logger log = LoggerFactory.getLogger(UserRepository.class);

	/**
	 * Finds a user by email.
	 *
	 * @param email
	 * @return Optional<User>
	 */
	public default Optional<User> findByEmail(String email) {
		log.debug("Finding user by email [{}]", email);
		JPQLQuery<User> query = select().where(QUser.user.email.equalsIgnoreCase(email));
		return Optional.ofNullable(query.fetchFirst());
	}

	/**
	 * Gets a user by verify token.
	 *
	 * @param verifyToken
	 * @return User
	 * @throws EntityNotFoundException
	 */
	public default User getByVerifyToken(UUID verifyToken) throws EntityNotFoundException {
		log.debug("Get user by verifyToken [{}]", verifyToken);
		JPQLQuery<User> query = select().where(QUser.user.verifyToken.eq(verifyToken));
		return getByQuery(query, () -> new EntityNotFoundException(String.format("No user found by verifyToken [%s]", verifyToken)));
	}

	/**
	 * Gets a user by reset token.
	 *
	 * @param resetToken
	 * @return User
	 * @throws EntityNotFoundException
	 */
	public default User getByResetToken(UUID resetToken) throws EntityNotFoundException {
		log.debug("Get user by resetToken [{}]", resetToken);
		JPQLQuery<User> query = select().where(QUser.user.resetToken.eq(resetToken));
		return getByQuery(query, () -> new EntityNotFoundException(String.format("No user found by resetToken [%s]", resetToken)));
	}

}
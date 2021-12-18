package com.sample.api.service.user;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.sample.api.service.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * User
 *
 * Stores the account data for a user in an application.
 *
 * @author Anthony DePalma
 */
@Getter
@Setter
@Entity
@Table(indexes = {
		@Index(columnList = "resetToken"),
		@Index(columnList = "verifyToken")
})
public class User extends AbstractEntity {

	@Email
	@NotNull
	@Column(unique = true)
	private String email;

	@NotNull
	private String password;

	@NotNull
	private Instant passwordDate;

	@NotNull
	@Enumerated(EnumType.STRING)
	private UserAuthority authority;

	@NotNull
	private UUID resetToken;

	@NotNull
	private UUID verifyToken;

	@Basic
	private boolean enabled;

	@Basic
	private boolean verified;

	// constructor
	public User() {

	}

	// constructor
	public User(UUID id) {
		super(id);
	}

}
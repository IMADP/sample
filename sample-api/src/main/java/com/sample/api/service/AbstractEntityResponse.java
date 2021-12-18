package com.sample.api.service;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

/**
 * AbstractEntityResponse
 *
 * Entity classes should extend this class and expose additional propeties as needed.
 *
 * @author Anthony DePalma
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public abstract class AbstractEntityResponse {
	private UUID id;
	private Long version;
	private Instant timeCreated;
	private Instant timeModified;

	// constructor
	public AbstractEntityResponse(AbstractEntity entity) {
		this.id = entity.getId();
		this.version = entity.getVersion();
		this.timeCreated = entity.getTimeCreated();
		this.timeModified = entity.getTimeModified();
	}

}

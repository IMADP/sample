package com.sample.api.service;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * AbstractEntity
 *
 * All entity objects should extend this class, or any class subclassing this one.
 *
 * @author Anthony DePalma
 */
@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AbstractEntity implements Serializable {

	/*
	 * The id represents the surrogate key that uniquely identifies this entity.
	 * It is important that the id is generated immediately on object creation to ensure immutability in collections.
	 */
	@Id
	@EqualsAndHashCode.Include
	private UUID id = UUID.randomUUID();

	/*
	 * The version keeps count of the number of changes to the entity.
	 * A null version represents an unsaved entity.
	 */
	@Version
	private Long version;

	/*
	 * The timeCreated is populated when the entity is persisted for the first time.
	 * It is often used for auditing purposes or for ordering entities according to their creation time.
	 * The Instant is used to keep this value standardized without time zone rules for reliable sorting.
	 * It is important not to rely on this field for business logic, such as the date of placing an order,
	 * since that date may be independent of when the entity was persisted in a database.
	 */
	@CreatedDate
	private Instant timeCreated;

	/*
	 * The timeModified is populated every time the entity is modified.
	 * The Instant is used to keep this value standardized without time zone rules for reliable sorting.
	 * Like timeCreated, it is important not to rely on this field for business logic.
	 */
	@LastModifiedDate
	private Instant timeModified;

	// constructor
	public AbstractEntity() {

	}

	// constructor for a shell entity to persist dependent relationships without querying the entire object
	public AbstractEntity(UUID id) {
		this.id = id;
		this.version = 0L;
	}

	/**
	 * Returns true if the entity is unsaved.
	 *
	 * @return boolean
	 */
	public final boolean isUnsaved() {
		return version == null;
	}

}
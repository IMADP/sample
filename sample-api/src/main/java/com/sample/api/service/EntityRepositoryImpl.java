package com.sample.api.service;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * EntityRepositoryImpl
 * 
 * Implementation of the EntityRepository.
 *
 * @param <T>
 * @author Anthony DePalma
 */
public class EntityRepositoryImpl <T extends AbstractEntity> extends SimpleJpaRepository<T, UUID> implements EntityRepository<T> {

	// the entity manager
	private final EntityManager entityManager;

	// the entity path
	private final EntityPath<T> entityPath;

	// constructor
	public EntityRepositoryImpl(JpaEntityInformation<T, String> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
		this.entityPath = SimpleEntityPathResolver.INSTANCE.createPath(entityInformation.getJavaType());
	}

	@Override
	public JPQLQuery<T> select() {
		JPQLQuery<T> query = new JPAQuery<>(entityManager);
		return query.from(entityPath);
	}

	@Override
	public T getById(UUID id) {
		Optional<T> optional = super.findById(id);
		return optional.orElseThrow(() -> new EntityNotFoundException(String.format("No entity [%s] with id [%s]", entityPath, id.toString())));
	}

	@Override
	public T getByQuery(JPQLQuery<T> query, Supplier<EntityNotFoundException> exceptionSupplier) throws EntityNotFoundException {
		Optional<T> optional = Optional.ofNullable(query.fetchOne());
		return optional.orElseThrow(exceptionSupplier);
	}

}
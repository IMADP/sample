package com.sample.api.service;

import java.util.UUID;
import java.util.function.Supplier;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.querydsl.jpa.JPQLQuery;


/**
 * EntityRepository
 *
 * Provides a way of retrieving data from a repository through common retrieval operations.
 * Repositories may need to extend this interface to execute more complicated queries.
 *
 * @param <T>
 * @author Anthony DePalma
 */
@NoRepositoryBean
public interface EntityRepository<T extends AbstractEntity> extends PagingAndSortingRepository<T, UUID> {

	/**
	 * Returns a JPQL select query for an entity.
	 *
	 * @return JPQLQuery<T>
	 */
	public JPQLQuery<T> select();

	/**
	 * Gets an entity by id, or throws an EntityNotFoundException.
	 *
	 * @param id
	 * @return T
	 * @throws EntityNotFoundException
	 */
	public T getById(UUID id) throws EntityNotFoundException;

	/**
	 * Gets an entity by a query, or throws an EntityNotFoundException.
	 *
	 * @param query
	 * @param exceptionSupplier
	 * @return T
	 * @throws EntityNotFoundException
	 */
	public T getByQuery(JPQLQuery<T> query, Supplier<EntityNotFoundException> exceptionSupplier) throws EntityNotFoundException;

}

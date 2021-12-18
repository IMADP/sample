package com.sample.api.service.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.JPQLQuery;
import com.sample.api.service.EntityRepository;

/**
 * EmailRepository
 *
 * Provides common retrieval operations for entities.
 *
 * @author Anthony DePalma
 */
@Repository
interface EmailRepository extends EntityRepository<Email> {

	// logger
	final Logger log = LoggerFactory.getLogger(EmailRepository.class);

	/**
	 * Returns true if an email was sent with the given template and the destination address.
	 *
	 * @param template
	 * @param toAddress
	 * @param correlationId
	 * @return boolean
	 */
	public default boolean isEmailSent(EmailTemplate template, String correlationId, String toAddress) {
		log.debug("Is email sent by template [{}], correlationId [{}], and toAddress [{}]", template, correlationId, toAddress);

		JPQLQuery<Email> query = select().where(QEmail.email.template.eq(template)
				.and(QEmail.email.correlationId.contains(correlationId))
				.and(QEmail.email.toAddress.contains(toAddress)));

		return query.fetchCount() > 0;
	}

}
package com.sample.api.service.email;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.sample.api.SampleApiProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * EmailService
 *
 * @author Anthony DePalma
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

	// properties
	private final JavaMailSender mailSender;
	private final EmailRepository emailRepository;
	private final SpringTemplateEngine templateEngine;
	private final MessageSource messageSource;
	private final SampleApiProperties properties;

	/**
	 * Sends and persists an email.
	 *
	 * @param email
	 * @return Email
	 */
	@Transactional
	public Email sendEmail(Email email) {
		log.debug("Sending email [{}]", email);
		sendEmailAsync(email);
		return emailRepository.save(email);
	}

	/**
	 * Sends an email once as determined by the template, correlationId, and toAddress.
	 * Returns true if the email was sent, false otherwise.
	 *
	 * @param email
	 * @return Optional<Email>
	 */
	public Optional<Email> sendEmailOnce(Email email) {
		log.debug("Is email sent by template [{}], correlationId [{}], and toAddress [{}]",
				email.getTemplate(), email.getCorrelationId(), email.getToAddress());

		// if an email was found, return empty
		if(emailRepository.isEmailSent(email.getTemplate(), email.getCorrelationId(), email.getToAddress()))
			return Optional.empty();

		return Optional.of(sendEmail(email));
	}

	@Async
	private void sendEmailAsync(Email email) {

		if(!properties.isEmailSend()) {
			log.info("Mocking email [{}]", ToStringBuilder.reflectionToString(email, ToStringStyle.MULTI_LINE_STYLE));
			return;
		}

		// send the email via the mail sender
		mailSender.send(email.toMimeMessagePreparator());
	}

	/**
	 * Creates an email from the given template.
	 *
	 * @param template
	 * @param context
	 * @return Email
	 */
	public Email createEmail(EmailTemplate template, Map<String, Object> context) {
		return createEmail(template, context, true);
	}

	/**
	 * Creates an email from the given template.
	 *
	 * @param template
	 * @param context
	 * @param html
	 * @return Email
	 */
	public Email createEmail(EmailTemplate template, Map<String, Object> context, boolean html) {

		// for internationalization the locale could be stored in a thread local variable
		Locale locale = Locale.ENGLISH;

		// create a new email
		Email email = new Email();
		email.setHtml(html);

		// resolve and substitue any values from properties
		email.setToAddress(resolveProperty(template, "toAddress", context, locale));
		email.setCcAddress(resolveProperty(template, "ccAddress", context, locale));
		email.setBccAddress(resolveProperty(template, "bccAddress", context, locale));
		email.setFromAddress(resolveProperty(template, "fromAddress", context, locale));
		email.setFromAlias(resolveProperty(template, "fromAlias", context, locale));
		email.setReplyToAddress(resolveProperty(template, "replyToAddress", context, locale));
		email.setReplyToAlias(resolveProperty(template, "replyToAlias", context, locale));
		email.setSubject(resolveProperty(template, "subject", context, locale));

		// invoke the template engine to render the content
		email.setTemplate(template);
		email.setText(templateEngine.process(template.getPath(), new Context(locale, context)));

		return email;
	}

	/**
	 * Resolve and substitute values in properties.
	 *
	 * @param template
	 * @param property
	 * @param context
	 * @return
	 */
	private String resolveProperty(EmailTemplate template, String property, Map<String, ?> context, Locale locale) {

		// read the property value from the messages properties
		String key = String.format("emailTemplate.%s.%s", template.toLowerCamel(), property);
		String value = messageSource.getMessage(key, null, null, locale);

		// subsitute any variables with parameters in the context map
		StringSubstitutor stringSubstitutor = new StringSubstitutor((k) -> {
			try {
				return BeanUtils.getProperty(context, k);
			} catch (Exception e) {
				return null;
			}
		});

		return stringSubstitutor.replace(value);
	}

}
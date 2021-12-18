package com.sample.api.service.email;

import java.util.List;

import javax.mail.internet.MimeMessage;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.sample.api.service.AbstractEntity;
import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * Email
 *
 * @author Anthony DePalma
 */
@Getter
@Setter
@Entity
@Table(indexes = {@Index(columnList = "correlationId, toAddress, template")})
public class Email extends AbstractEntity {

	@NotNull
	private String toAddress;

	@Basic
	private String ccAddress;

	@Basic
	private String bccAddress;

	@Basic
	private String fromAddress;

	@Basic
	private String fromAlias;

	@Basic
	private String replyToAddress;

	@Basic
	private String replyToAlias;

	@Basic
	private String subject;

	@Enumerated(EnumType.STRING)
	private EmailTemplate template;

	@Basic
	private String correlationId;

	@Lob
	@NotNull
	private String text;

	@Basic
	private boolean html;

	// constructor
	public Email() {

	}

	// constructor
	public Email(Email email) {
		this.toAddress = email.toAddress;
		this.ccAddress = email.ccAddress;
		this.bccAddress = email.bccAddress;
		this.fromAddress = email.fromAddress;
		this.fromAlias = email.fromAlias;
		this.replyToAddress = email.replyToAddress;
		this.replyToAlias = email.replyToAlias;
		this.subject = email.subject;
		this.text = email.text;
		this.template = email.template;
		this.correlationId = email.correlationId;
		this.html = email.html;
	}

	/**
	 * Returns a MimeMessagePreparator from the email instance.
	 *
	 * @return MimeMessagePreparator
	 */
	MimeMessagePreparator toMimeMessagePreparator() {
		return new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

				if(toAddress != null)
					message.setTo(getToAddressArray());

				if(bccAddress != null)
					message.setBcc(getBccAddressArray());

				if(ccAddress != null)
					message.setCc(getCcAddressArray());

				if(subject != null)
					message.setSubject(subject);

				if(text != null)
					message.setText(text, html);

				if(fromAddress != null && fromAlias == null)
					message.setFrom(fromAddress);

				if(fromAddress != null && fromAlias != null)
					message.setFrom(fromAddress, fromAlias);

				if(replyToAddress != null && replyToAlias == null)
					message.setReplyTo(replyToAddress);

				if(replyToAddress != null && replyToAlias != null)
					message.setReplyTo(replyToAddress, replyToAlias);
			}
		};
	}

	// delegate getters and setters
	public String[] getToAddressArray() {
		return StringUtils.split(toAddress, ",");
	}

	public void setToAddressArray(String[] toAddressArray) {
		this.toAddress = StringUtils.join(toAddressArray, ",");
	}

	public void setToAddressList(List<String> toAddressList) {
		this.toAddress = StringUtils.join(toAddressList, ",");
	}

	public String[] getCcAddressArray() {
		return StringUtils.split(ccAddress, ",");
	}

	public void setCcAddressArray(String[] ccAddressArray) {
		this.ccAddress = StringUtils.join(ccAddressArray, ",");
	}

	public void setCcAddressList(List<String> ccAddressList) {
		this.ccAddress = StringUtils.join(ccAddressList, ",");
	}

	public String[] getBccAddressArray() {
		return StringUtils.split(bccAddress, ",");
	}

	public void setBccAddressArray(String[] bccAddressArray) {
		this.bccAddress = StringUtils.join(bccAddressArray, ",");
	}

	public void setBccAddressList(List<String> bccAddressList) {
		this.bccAddress = StringUtils.join(bccAddressList, ",");
	}

}

package com.sample.api.service.email;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

/**
 * EmailProperties
 *
 * @author Anthony DePalma
 */
@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "email")
public class EmailProperties {

	@NotNull
	private boolean live;

}
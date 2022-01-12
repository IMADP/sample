package com.sample.api;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.sample.api.service.EntityRepositoryImpl;

import lombok.RequiredArgsConstructor;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@EnableWebMvc
@EnableScheduling
@EnableWebSecurity
@EnableJpaAuditing
@EnableAsync(mode = AdviceMode.ASPECTJ)
@EnableCaching(mode = AdviceMode.ASPECTJ)
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties
@EnableLoadTimeWeaving(aspectjWeaving = AspectJWeaving.ENABLED)
@EnableJpaRepositories(repositoryBaseClass = EntityRepositoryImpl.class, basePackages = SampleApi.BASE_PACKAGE)
@Import(SampleApiSecurity.class)
@RequiredArgsConstructor
@SpringBootApplication
public class SampleApi {

	// properties
	private final SampleApiProperties properties;

	// the base package of the application
	public static final String BASE_PACKAGE = "com.sample.api";

	/**
	 * Entry point into the application.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(SampleApi.class, args);
	}

	@Bean
	public Docket api() {
		ApiInfo appInfo = new ApiInfoBuilder()
				.title(properties.getAppTitle())
				.description(properties.getAppDescription())
				.version(properties.getAppVersion())
				.build();

		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(appInfo)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/api/**"))
				.build();
	}

	@Bean
	public CloseableHttpClient closeableHttpClient() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(400);
		connectionManager.setDefaultMaxPerRoute(200);

		RequestConfig requestConfig = RequestConfig
				.custom()
				.setConnectTimeout(60000)
				.setConnectionRequestTimeout(60000)
				.setSocketTimeout(60000)
				.build();

		return HttpClients
				.custom()
				.setDefaultRequestConfig(requestConfig)
				.setConnectionManager(connectionManager)
				.disableAutomaticRetries()
				.disableCookieManagement()
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

}

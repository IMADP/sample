package com.sample.api.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * HttpClient
 *
 * Utility for making http connections.
 *
 * @author Anthony DePalma
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HttpClient {

	// properties
	private final CloseableHttpClient closeableHttpClient;

	/**
	 * Executes a request and returns the response.
	 *
	 * @param request
	 * @return HttpResponse
	 */
	public final HttpResponse execute(HttpRequest request) {
		try
		{
			log.trace("-> {}", request);
			log.debug("-> {} {}", request.getMethod(), request.getUri());

			// create request
			HttpContext context = HttpClientContext.create();
			HttpUriRequest uriRequest = getHttpUriRequest(request);

			// apply headers
			request.getHeaders().getHeaderMap().entrySet().stream().forEach(e -> uriRequest.addHeader(e.getKey(), e.getValue()));

			// execute request
			try(CloseableHttpResponse closeableResponse = closeableHttpClient.execute(uriRequest, context))
			{
				HttpUri uri = request.getUri();
				HttpMethod method = request.getMethod();
				HttpHeaders headers = getHeaders(closeableResponse);
				HttpContent content = getContent(closeableResponse);
				HttpStatus status = getStatus(closeableResponse);
				HttpResponse response = HttpResponse.of(uri, method, headers, content, status);

				log.trace("<- {}", response);
				log.debug("<- {} {} ({})", method, uri, status);

				return response;
			}
		}
		catch (Exception exception)
		{
			throw new HttpException(request, null, exception);
		}
	}

	/**
	 * Creates an HttpUriRequest.
	 *
	 * @param request
	 * @return HttpUriRequest
	 * @throws URISyntaxException
	 */
	private HttpUriRequest getHttpUriRequest(HttpRequest request) throws URISyntaxException {
		URI uri = new URI(request.getUri().getUri());

		// these request methods do not support http entities
		if(HttpMethod.GET == request.getMethod()) {
			return new HttpGet(uri);
		}

		if(HttpMethod.DELETE == request.getMethod()) {
			return new HttpDelete(uri);
		}

		// these request methods support optional http entities
		if(HttpMethod.POST == request.getMethod()) {
			return setEntity(new HttpPost(uri), request.getContent());
		}

		if(HttpMethod.PATCH == request.getMethod()) {
			return setEntity(new HttpPatch(uri), request.getContent());
		}

		if(HttpMethod.PUT == request.getMethod()) {
			return setEntity(new HttpPut(uri), request.getContent());
		}

		// this should not be possible due to the HttpRequest static factory methods
		throw new IllegalArgumentException("Unsupported HTTP Method: " + request.getMethod());
	}

	/**
	 * Sets the content as an http entity, if provided.
	 *
	 * @param uriRequest
	 * @param content
	 * @return HttpUriRequest
	 */
	private HttpUriRequest setEntity(HttpEntityEnclosingRequestBase uriRequest, HttpContent content) {

		// if no content is provided return the request
		if(content == null) {
			return uriRequest;
		}

		// convert the content to an http entity
		HttpEntity entity = new StringEntity(content.getContent(), content.getType());
		uriRequest.setEntity(entity);
		return uriRequest;
	}

	/**
	 * Gets HttpHeaders from a response.
	 *
	 * @param closeableResponse
	 * @return HttpHeaders
	 */
	private HttpHeaders getHeaders(CloseableHttpResponse closeableResponse) {
		Map<String, String> headerMap = new HashMap<>();
		Arrays.stream(closeableResponse.getAllHeaders()).forEach(h -> headerMap.put(h.getName(), h.getValue()));
		return HttpHeaders.of(headerMap);
	}

	/**
	 * Gets HttpStatus from a response.
	 *
	 * @param closeableResponse
	 * @return HttpStatus
	 */
	private HttpStatus getStatus(CloseableHttpResponse closeableResponse) {
		return HttpStatus.resolve(closeableResponse.getStatusLine().getStatusCode());
	}

	/**
	 * Gtes HttpContent from a response.
	 *
	 * @param closeableResponse
	 * @return HttpContent
	 * @throws IOException
	 */
	private HttpContent getContent(CloseableHttpResponse closeableResponse) throws IOException {

		// if no entity is found return null
		if(closeableResponse.getEntity() == null) {
			return null;
		}

		String content = new String(closeableResponse.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
		ContentType type = ContentType.parse(closeableResponse.getEntity().getContentType().getValue());
		return new HttpContent(content, type);
	}

}
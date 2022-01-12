package com.sample.api.http;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import com.sample.api.service.JsonMapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * HttpContent
 *
 * @author Anthony DePalma
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class HttpContent {

	/**
	 * Creates an HttpContent out of text.
	 *
	 * @param object
	 * @return HttpContent
	 */
	public static HttpContent ofText(String text) {
		return new HttpContent(text, ContentType.TEXT_PLAIN);
	}

	/**
	 * Creates an HttpContent out of json.
	 *
	 * @param object
	 * @return HttpContent
	 */
	public static HttpContent ofJson(Object object) {
		return new HttpContent(JsonMapper.toJson(object), ContentType.APPLICATION_JSON);
	}

	/**
	 * Creates an HttpContent out of form parameters.
	 *
	 * @param paramMap
	 * @return HttpContent
	 */
	public static HttpContent ofParams(Map<String, String> paramMap) {

		// map to name value pairs
		List<NameValuePair> params = paramMap.entrySet().stream().map(
				e -> new BasicNameValuePair(e.getKey(), e.getValue())).collect(Collectors.toList());

		String body = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
		ContentType type = ContentType.create(URLEncodedUtils.CONTENT_TYPE, StandardCharsets.UTF_8);
		return new HttpContent(body, type);
	}

	// the content
	private final String content;

	// the type
	private final ContentType type;

	@Override
	public String toString() {
		return String.format("(%s) %s", type.getMimeType(), content);
	}

}
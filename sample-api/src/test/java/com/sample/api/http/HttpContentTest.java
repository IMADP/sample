package com.sample.api.http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;

import com.sample.api.SampleApiTest;

/**
 * HttpContentTest
 *
 * @author Anthony DePalma
 */
public class HttpContentTest extends SampleApiTest {

	@Test
	public void text() {
		String text = "text";
		HttpContent httpContent = HttpContent.ofText(text);
		assertEquals("text", httpContent.getContent());

		ContentType type = ContentType.TEXT_PLAIN;
		assertEquals(type.getCharset(), httpContent.getType().getCharset());
		assertEquals(type.getMimeType(), httpContent.getType().getMimeType());
	}

	@Test
	public void json() {
		Map<String, String> map = Map.of("key", "value");
		HttpContent httpContent = HttpContent.ofJson(map);
		assertEquals("{\"key\":\"value\"}", httpContent.getContent());

		ContentType type = ContentType.APPLICATION_JSON;
		assertEquals(type.getCharset(), httpContent.getType().getCharset());
		assertEquals(type.getMimeType(), httpContent.getType().getMimeType());
	}

	@Test
	public void params() {
		Map<String, String> map = Map.of("sort", "asc");
		HttpContent httpContent = HttpContent.ofParams(map);
		assertEquals("sort=asc", httpContent.getContent());

		ContentType type = ContentType.create(URLEncodedUtils.CONTENT_TYPE, StandardCharsets.UTF_8);
		assertEquals(type.getCharset(), httpContent.getType().getCharset());
		assertEquals(type.getMimeType(), httpContent.getType().getMimeType());
	}

}

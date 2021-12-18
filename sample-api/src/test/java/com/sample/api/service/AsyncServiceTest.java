package com.sample.api.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sample.api.SampleApiTest;

/**
 * AsyncServiceTest
 *
 * @author Anthony DePalma
 */
public class AsyncServiceTest extends SampleApiTest {

	@Autowired
	private AsyncService asyncService;

	@Test
	public void publicAsync() {
		asyncService.publicAsync();
	}

	@Test
	public void protectedAsync() {
		asyncService.protectedAsync();
	}

	@Test
	public void privateAsync() {
		asyncService.privateAsync();
	}

	@Test
	public void finalAsync() {
		asyncService.finalAsync();
	}

}

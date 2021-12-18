package com.sample.api.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * AsyncService
 *
 * @author Anthony DePalma
 */
@Service
public class AsyncService {

	@Async
	public void publicAsync() {
		throw new Error();
	}

	@Async
	protected void protectedAsync() {
		throw new Error();
	}

	public void privateAsync() {
		selfInvokedAsync();
	}

	@Async
	private void selfInvokedAsync() {
		throw new Error();
	}

	@Async
	public final void finalAsync() {
		throw new Error();
	}

}
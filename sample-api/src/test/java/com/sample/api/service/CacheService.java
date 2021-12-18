package com.sample.api.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * CacheService
 *
 * @author Anthony DePalma
 */
@Service
public class CacheService {

	@Cacheable(key = "'PUBLIC'", cacheNames = "CACHE")
	public boolean publicCache() {
		return true;
	}

	@Cacheable(key = "'PROTECTED'", cacheNames = "CACHE")
	protected boolean protectedCache() {
		return true;
	}

	public boolean privateCache() {
		return selfInvokedCache();
	}

	@Cacheable(key = "'PRIVATE'", cacheNames = "CACHE")
	private boolean selfInvokedCache() {
		return true;
	}

	@Cacheable(key = "'FINAL'", cacheNames = "CACHE")
	public final boolean finalCache() {
		return true;
	}

}
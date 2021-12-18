package com.sample.api.service;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

import com.sample.api.SampleApiTest;

/**
 * CacheServiceTest
 *
 * @author Anthony DePalma
 */
public class CacheServiceTest extends SampleApiTest {

	@Autowired
	private CacheService cacheService;

	@Autowired
	private CacheManager cacheManager;

	@Test
	public void publicCache() {
		Cache cache = cacheManager.getCache("CACHE");
		ValueWrapper wrapper = cache.get("PUBLIC");
		assertNull(wrapper);

		cacheService.publicCache();
		wrapper = cache.get("PUBLIC");

		assertNotNull(wrapper);
		assertTrue((boolean) wrapper.get());
	}

	@Test
	public void protectedCache() {
		Cache cache = cacheManager.getCache("CACHE");
		ValueWrapper wrapper = cache.get("PROTECTED");
		assertNull(wrapper);

		cacheService.protectedCache();
		wrapper = cache.get("PROTECTED");

		assertNotNull(wrapper);
		assertTrue((boolean) wrapper.get());
	}

	@Test
	public void privateCache() {
		Cache cache = cacheManager.getCache("CACHE");
		ValueWrapper wrapper = cache.get("PRIVATE");
		assertNull(wrapper);

		cacheService.privateCache();
		wrapper = cache.get("PRIVATE");

		assertNotNull(wrapper);
		assertTrue((boolean) wrapper.get());
	}

	@Test
	public void finalCache() {
		Cache cache = cacheManager.getCache("CACHE");
		ValueWrapper wrapper = cache.get("FINAL");
		assertNull(wrapper);

		cacheService.finalCache();
		wrapper = cache.get("FINAL");

		assertNotNull(wrapper);
		assertTrue((boolean) wrapper.get());
	}

}

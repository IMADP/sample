package com.sample.api;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class SampleApiTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	public void truncateDatabase() {
		jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

		for(Map<String, Object> map : jdbcTemplate.queryForList("SHOW TABLES"))
			jdbcTemplate.execute("TRUNCATE TABLE " + map.get("TABLE_NAME"));

		jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
	}

	@Test
	public void contextLoads() {

	}

}

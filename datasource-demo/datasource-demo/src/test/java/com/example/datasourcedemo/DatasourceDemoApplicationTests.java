package com.example.datasourcedemo;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatasourceDemoApplicationTests {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test(expected = CustomDuplicatedKeyException.class)
	public void testThrowingCustomException() {
		jdbcTemplate.execute("insert into FOO (ID, BAR) VALUES (4, 'VVV')");
		jdbcTemplate.execute("insert into FOO (ID, BAR) VALUES (4, 'AAC')");
	}

}

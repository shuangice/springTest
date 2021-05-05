package com.druiddemo.druiddemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FooService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void selectForUpdate() {
        jdbcTemplate.queryForObject("select id from FOO where id = 1 for update ", Long.class);

        try {
            Thread.sleep(200);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

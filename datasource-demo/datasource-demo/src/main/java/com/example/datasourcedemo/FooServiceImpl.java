package com.example.datasourcedemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FooServiceImpl implements FooService{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FooService fooService;

    @Override
    @Transactional
    public void insertRecord() {
        jdbcTemplate.execute("insert into FOO (BAR) VALUES ('AAA') ");
    }

    @Override
    @Transactional(rollbackFor = RollbackException.class /*propagation = Propagation.NESTED*/)
    public void insertThenRollback() throws RollbackException {
        jdbcTemplate.execute("insert into FOO (BAR) VALUES ('BBB') ");
        throw new RollbackException();
    }

    @Override
   // @Transactional(rollbackFor = RuntimeException.class)
    public void invokeInsertThenRollback() throws RollbackException {

     /*   jdbcTemplate.execute("insert into FOO (BAR) VALUES ('CCC') ");*/

        fooService.insertThenRollback();

     /*   throw new RuntimeException();*/
    }
}

package com.jdbc.demo.jbdc_demo.dao;

import com.jdbc.demo.jbdc_demo.Foo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class BatchFooDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void batchInsert() {
        jdbcTemplate.batchUpdate("insert into FOO (BAR) values (?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setString(1,"b-"+i);
                    }

                    @Override
                    public int getBatchSize() {
                        return 2;
                    }
                });

        ArrayList<Foo> list = new ArrayList<>();
        list.add(Foo.builder().id(100L).bar("c-100").build());
        list.add(Foo.builder().id(101L).bar("c-101").build());

        namedParameterJdbcTemplate
                .batchUpdate("insert into FOO (ID, BAR) VALUES (:id, :bar)"
                        , SqlParameterSourceUtils.createBatch(list));
    }
}

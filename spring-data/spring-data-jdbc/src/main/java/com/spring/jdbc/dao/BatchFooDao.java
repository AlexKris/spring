package com.spring.jdbc.dao;

import com.spring.jdbc.entity.Foo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BatchFooDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void batchInsert() {
        jdbcTemplate.batchUpdate("insert into foo (bar) values (?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, "b-" + i);
                    }

                    @Override
                    public int getBatchSize() {
                        return 2;
                    }
                });

        List<Foo> list = new ArrayList<>();
        list.add(Foo.builder().id(100L).bar("b-100").build());
        list.add(Foo.builder().id(101L).bar("b-101").build());
        namedParameterJdbcTemplate.batchUpdate("insert into foo (id, bar) values (:id, :bar);",
                SqlParameterSourceUtils.createBatch(list));
    }
}

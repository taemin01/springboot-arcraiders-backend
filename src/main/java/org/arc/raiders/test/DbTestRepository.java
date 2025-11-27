package org.arc.raiders.test;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DbTestRepository {

    private final JdbcTemplate jdbcTemplate;

    public DbTestRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer testConnection() {
        return jdbcTemplate.queryForObject("SELECT 1", Integer.class);
    }
}

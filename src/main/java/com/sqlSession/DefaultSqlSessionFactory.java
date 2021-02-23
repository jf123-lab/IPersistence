package com.sqlSession;

import com.config.Configuration;

import java.sql.SQLException;

public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() throws SQLException {

        return new DefaultSqlSession(configuration);
    }
}

package com.config;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 存放数据源及mappedStatement
 */
public class Configuration {
    private DataSource dataSource;

    private Map<String, MappedStatement> mappedStatement = new HashMap<String, MappedStatement>();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatement> getMappedStatment() {
        return mappedStatement;
    }

    public void setMappedStatment(Map<String, MappedStatement> mappedStatement) {
        this.mappedStatement = mappedStatement;
    }
}

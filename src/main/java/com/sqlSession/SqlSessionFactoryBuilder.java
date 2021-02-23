package com.sqlSession;

import com.config.Configuration;
import com.config.XMLConfigBuilder;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * sqlSessionFactory构造器
 */
public class SqlSessionFactoryBuilder {

    private Configuration configuration;

    public SqlSessionFactoryBuilder() {
        this.configuration = new Configuration();
    }

    public SqlSessionFactory build(InputStream inputStream) throws DocumentException, PropertyVetoException, ClassNotFoundException {

        //解析配置文件
        Configuration configuration = new XMLConfigBuilder(this.configuration).parseConfiguration(inputStream);

        //创建sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return sqlSessionFactory;
    }
}

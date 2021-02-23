package com.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XMLConfigBuilder {
    private Configuration configuration;

    public XMLConfigBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration parseConfiguration(InputStream inputStream) throws DocumentException, PropertyVetoException, ClassNotFoundException {
        Document document = new SAXReader().read(inputStream);
        //<configuration>
        Element rootElement = document.getRootElement();
        List<Element> propertyElements = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (Element propertyElement : propertyElements) {
            String name = propertyElement.attributeValue("name");
            String value = propertyElement.attributeValue("value");
            properties.put(name,  value);
        }
        //连接池
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("user"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        //填充configuration
        configuration.setDataSource(comboPooledDataSource);
        //解析mapper部分
        List<Element> mapperElements = rootElement.selectNodes("//mapper");
        for (Element mapperElement : mapperElements) {
            String resource = mapperElement.attributeValue("resource");
            InputStream resourceAsStream = Resources.getResourceAsStream(resource);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
            xmlMapperBuilder.parse(resourceAsStream);
        }
        return configuration;
    }
}

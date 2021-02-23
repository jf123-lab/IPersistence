package com.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder {
    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException, ClassNotFoundException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");
        //解析select标签
        List<Element> elementList = rootElement.selectNodes("select");
        for (Element element : elementList) {
            String id = element.attributeValue("id");
            String parameterType = element.attributeValue("parameterType");
            String resultType = element.attributeValue("resultType");
            Class<?> parameterTypeClass = getClassType(parameterType);
            Class<?> resultTypeClass = getClassType(resultType);
            MappedStatement mappedStatement = new MappedStatement();
            //StatementId = namespace.id
            String statementId = namespace + "." + id;
            //sql
            String textTrimSQL = element.getTextTrim();
            mappedStatement.setId(id);
            mappedStatement.setParameterType(parameterTypeClass);
            mappedStatement.setResultType(resultTypeClass);
            mappedStatement.setSql(textTrimSQL);
            //填充configuration
            configuration.getMappedStatment().put(statementId, mappedStatement);
        }
        //解析update
        List<Element> elementUpdate = rootElement.selectNodes("update");
        for (Element element : elementUpdate) {
            String id = element.attributeValue("id");
            String parameterType = element.attributeValue("parameterType");
            Class<?> parameterTypeClass = getClassType(parameterType);
            MappedStatement mappedStatement = new MappedStatement();
            //StatementId = namespace.id
            String statementId = namespace + "." + id;
            //sql
            String textTrimSQL = element.getTextTrim();
            mappedStatement.setId(id);
            mappedStatement.setParameterType(parameterTypeClass);
            mappedStatement.setResultType(Integer.class);
            mappedStatement.setSql(textTrimSQL);
            //填充configuration
            configuration.getMappedStatment().put(statementId, mappedStatement);
        }
        //解析update
        List<Element> insertElement = rootElement.selectNodes("insert");
        for (Element element : insertElement) {
            String id = element.attributeValue("id");
            String parameterType = element.attributeValue("parameterType");
            Class<?> parameterTypeClass = getClassType(parameterType);
            MappedStatement mappedStatement = new MappedStatement();
            //StatementId = namespace.id
            String statementId = namespace + "." + id;
            //sql
            String textTrimSQL = element.getTextTrim();
            mappedStatement.setId(id);
            mappedStatement.setParameterType(parameterTypeClass);
            mappedStatement.setResultType(Integer.class);
            mappedStatement.setSql(textTrimSQL);
            //填充configuration
            configuration.getMappedStatment().put(statementId, mappedStatement);
        }

        //解析update
        List<Element> deleteElement = rootElement.selectNodes("delete");
        for (Element element : deleteElement) {
            String id = element.attributeValue("id");
            String parameterType = element.attributeValue("parameterType");
            Class<?> parameterTypeClass = getClassType(parameterType);
            MappedStatement mappedStatement = new MappedStatement();
            //StatementId = namespace.id
            String statementId = namespace + "." + id;
            //sql
            String textTrimSQL = element.getTextTrim();
            mappedStatement.setId(id);
            mappedStatement.setParameterType(parameterTypeClass);
            mappedStatement.setResultType(Integer.class);
            mappedStatement.setSql(textTrimSQL);
            //填充configuration
            configuration.getMappedStatment().put(statementId, mappedStatement);
        }
    }

    //根据类的路径获取类
    private Class<?> getClassType(String path) throws ClassNotFoundException {
        if ("".equals(path) || path == null) {
            return null;
        }
        Class<?> aClass = Class.forName(path);
        return aClass;
    }
}

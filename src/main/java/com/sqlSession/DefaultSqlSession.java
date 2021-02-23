package com.sqlSession;

import com.config.Configuration;
import com.config.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private Executor executor = new SimpleExector();


    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;

    }

    @Override
    public <T> List<T> selectList(String statementId, Object... param) throws IllegalAccessException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException {
        MappedStatement mappedStatement = configuration.getMappedStatment().get(statementId);
        List<T> query = executor.selectList(configuration, mappedStatement, param);

        return query;
    }

    @Override
    public <E> E selectOne(String statementId, Object... param) throws IllegalAccessException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException {
        List<Object> query = selectList(statementId, param);
        if (query.size() == 1) {
            return (E)query.get(0);
        } else {
            throw new RuntimeException("返回结果过多");
        }
    }

    @Override
    public int update(String statementId, Object... param) throws IllegalAccessException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException {
        MappedStatement mappedStatement = configuration.getMappedStatment().get(statementId);
        int result = executor.update(configuration, mappedStatement, param);
        return result;
    }

    @Override
    public int insert(String statementId, Object... param) throws IllegalAccessException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException {
        MappedStatement mappedStatement = configuration.getMappedStatment().get(statementId);
        int result = executor.update(configuration, mappedStatement, param);
        return result;
    }


    @Override
    public void close() throws SQLException {
        executor.close();
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        Object newProxyInstance = Proxy.newProxyInstance(mapperClass.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //selectOne
                String methodName = method.getName();
                //className
                String className = method.getDeclaringClass().getSimpleName();
                //statementid
                String key = className + "." + methodName;
                //MappedStatement mappedStatement = configuration.getMappedStatment().get(key);、
                if (methodName.startsWith("update")) {
                    return update(key, args);
                }

                if (methodName.startsWith("insert")) {
                    return insert(key, args);
                }

                if (methodName.startsWith("delete")) {
                    return deleteByCid(key, args);
                }
                //参数化类型
                Type genericReturnType = method.getGenericReturnType();
                if (genericReturnType instanceof ParameterizedType) {
                    List<Object> objects = selectList(key, args);
                    return objects;
                }

                return selectOne(key, args);
            }
        });

        return (T) newProxyInstance;
    }

    @Override
    public int deleteByCid(String statementId, Object... param) throws IllegalAccessException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException {
        MappedStatement mappedStatement = configuration.getMappedStatment().get(statementId);
        int result = executor.update(configuration, mappedStatement, param);
        return result;
    }
}

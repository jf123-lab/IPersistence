package com.sqlSession;

import com.config.Configuration;
import com.config.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface Executor {
    <E> List<E> selectList(Configuration configuration, MappedStatement mappedStatement, Object[] param) throws SQLException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException;

    void close() throws SQLException;

    int update(Configuration configuration, MappedStatement mappedStatement, Object[] param) throws SQLException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException;


}

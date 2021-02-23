package com.sqlSession;


import com.config.BoundSql;
import com.config.Configuration;
import com.config.MappedStatement;
import com.util.GenericTokenPaser;
import com.util.ParameterMapping;
import com.util.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExector implements Executor {

    private Connection connection = null;

    @Override
    public <E> List<E> selectList(Configuration configuration, MappedStatement mappedStatement, Object[] param) throws SQLException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException {
        //获取连接
        connection = configuration.getDataSource().getConnection();
        String sql = mappedStatement.getSql();
        //对sql进行处理
        BoundSql boundSql = getBoundSql(sql);
        //处理后的sql
        String finalSql = boundSql.getSqlText();
        //获取传入参数类型
        Class<?> parameterType = mappedStatement.getParameterType();
        //获取预编译对象
        PreparedStatement preparedStatement = connection.prepareStatement(finalSql);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();

        for (int i = 0; i < parameterMappingList.size(); i++) {
            if (parameterType == null) {
                preparedStatement.setObject(i + 1, null);
            } else {
                ParameterMapping parameterMapping = parameterMappingList.get(i);
                String name = parameterMapping.getContent();

                //反射
                Field declaredField = parameterType.getDeclaredField(name);
                declaredField.setAccessible(true);

                //参数值
                Object o = declaredField.get(param[i]);
                //设置参数值
                preparedStatement.setObject(i + 1, o);
            }
        }

        ResultSet resultSet = preparedStatement.executeQuery();

        //ResultSet resultSet = getResultSet(configuration, mappedStatement, param);

        Class<?> resultType = mappedStatement.getResultType();

        ArrayList<Object> objects = new ArrayList<>();
        while (resultSet.next()) {
            Object o = resultType.newInstance();
            //元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                //属性名
                String columnName = metaData.getColumnName(i);
                //属性值
                Object value = resultSet.getObject(columnName);
                //创建属性描述器，为属性生成读写方法
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultType);
                //获取写方法
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, value);
            }
            objects.add(o);
        }


        return (List<E>) objects;
    }

    @Override
    public void close() throws SQLException {
        connection.close();

    }

    @Override
    public int update(Configuration configuration, MappedStatement mappedStatement, Object[] param) throws SQLException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException {

        int resultSet = getResultSet(configuration, mappedStatement, param);
        return resultSet;
    }

    private BoundSql getBoundSql(String sql) {
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenPaser genericTokenPaser = new GenericTokenPaser("#{", "}", parameterMappingTokenHandler);
        String parse = genericTokenPaser.parse(sql);
        List<ParameterMapping> parameterMappingList = parameterMappingTokenHandler.getParameterMappingList();
        BoundSql boundSql = new BoundSql(parse, parameterMappingList);

        return boundSql;
    }


    private int getResultSet(Configuration configuration, MappedStatement mappedStatement, Object[] param) throws SQLException, NoSuchFieldException, IllegalAccessException {
        //获取连接
        connection = configuration.getDataSource().getConnection();
        String sql = mappedStatement.getSql();
        //对sql进行处理
        BoundSql boundSql = getBoundSql(sql);
        //处理后的sql
        String finalSql = boundSql.getSqlText();
        //获取传入参数类型
        Class<?> parameterType = mappedStatement.getParameterType();
        //获取预编译对象
        PreparedStatement preparedStatement = connection.prepareStatement(finalSql);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        ArrayList<Object> objects = new ArrayList<>();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            Field declaredField = null;
            Object o = null;
            if (parameterType == null) {
                preparedStatement.setObject(i + 1, null);
            } else {
                ParameterMapping parameterMapping = parameterMappingList.get(i);
                String name = parameterMapping.getContent();
                if (parameterType.getName().equals(Integer.class.getName())) {
                    o = param[i];
                } else {
                    //反射
                    declaredField = parameterType.getDeclaredField(name);
                    declaredField.setAccessible(true);
                    //参数值
                    o = declaredField.get(param[0]);
                }

                //设置参数值
                preparedStatement.setObject(i + 1, o);
            }
        }

        int resultSet = preparedStatement.executeUpdate();
        return resultSet;
    }

}

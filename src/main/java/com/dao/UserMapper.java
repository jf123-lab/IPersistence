package com.dao;

import com.pojo.User;

import java.util.List;

public interface UserMapper {
    //查询所有用户
    List<User> selectList() throws Exception;

    //查询所有用户
    User selectOne(User user) throws Exception;

    //修改用户

    int update(User user) throws Exception;

    int insert(User user) throws Exception;

    //根据条件进行用户查询
    User findByCondition(User user) throws Exception;

    int deleteByCid(int id) throws Exception;
}

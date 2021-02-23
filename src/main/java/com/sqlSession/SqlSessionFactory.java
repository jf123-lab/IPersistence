package com.sqlSession;

import java.sql.SQLException;

public interface SqlSessionFactory {
    SqlSession openSession() throws SQLException;
}

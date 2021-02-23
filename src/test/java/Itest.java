import com.config.Resources;
import com.dao.UserMapper;
import com.pojo.User;
import com.sqlSession.SqlSession;
import com.sqlSession.SqlSessionFactory;
import com.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Itest {
    SqlSession sqlSession;


    @Before
    public void before() throws SQLException, DocumentException, PropertyVetoException, ClassNotFoundException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapperConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        sqlSession = sqlSessionFactory.openSession();
    }

    @After
    public void after() throws SQLException {
        sqlSession.close();
    }

    @Test
    public void test1() throws Exception {
        User user = new User();
        user.setBirthday(new Date());
        user.setPassword("11123");
        user.setUsername("张三");
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int result = mapper.insert(user);
        System.out.println(result);
    }

    @Test
    public void test2() throws Exception {


        User user = new User();
        user.setId(6);
        user.setBirthday(new Date());
        user.setPassword("111332233");
        user.setUsername("王五");
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int result = mapper.update(user);
        System.out.println(result);

    }
    @Test
    public void test3() throws Exception {


        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = mapper.selectList();
        for (User user : users) {
            System.out.println(user);
        }


    }

    @Test
    public void test4() throws Exception {
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int i = mapper.deleteByCid(2);
        System.out.println(i);
    }
}

import com.study.mapper.UserMapper;
import com.study.pojo.User;
import com.study.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserMapperTest {
    @Test
    public void test(){
        SqlSession sqlSession= MybatisUtils.getSqlSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);

        List<User> userList = mapper.getUsers();
        for(User user:userList){
            System.out.println(user);
        }
        sqlSession.close();
    }
}

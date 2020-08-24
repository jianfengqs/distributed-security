import com.qs.security.distributed.uaa.UAAServer;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UAAServer.class)
public class Test {

    @Autowired
    private BCryptPasswordEncoder encoder;

    //配置表名
    private static final String TABLE_NAME = "t_sys_log_login";

    //配置作者名字
    private static final String AUTHOR = "jianfeng";

    @org.junit.Test
    public void execute() {
        String encode = encoder.encode("123");
        System.out.println(encode);


        String hashpw = BCrypt.hashpw("secret", BCrypt.gensalt());
        System.out.println(hashpw);
    }
}
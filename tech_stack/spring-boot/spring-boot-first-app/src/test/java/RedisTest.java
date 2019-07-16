import com.google.common.base.Joiner;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;
import xin.zero2one.SpringBootFirstApp;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoujundong
 * @data 6/19/2019
 * @description TODO
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootFirstApp.class)
public class RedisTest {


}

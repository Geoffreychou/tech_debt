package xin.zero2one.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;
import xin.zero2one.redis.RedisApplication;

import javax.annotation.Resource;

/**
 * @author ZJD
 * @date 2019/6/30
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisApplication.class)
@Slf4j
public class RedisTemplateTest {

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;


    @Test
    public void testRedisTemplate() {
        String key = "redisTemplate_key";
        String value = "1";
        ValueOperations operations = redisTemplate.opsForValue();
        operations.set(key, value);
        Object setValue = operations.get(key);
        log.info("set value success, value is: [{}]", setValue);
        Long increment = operations.increment(key);
        log.info("increase value success, value is: [{}]", increment);
    }

    @Test
    public void testRedisTemplate2() {
        String key = "redisTemplate_key_2";
        String value = "1";
        ValueOperations operations = redisTemplate.opsForValue();
        operations.increment(key, 1);
        Object setValue = operations.get(key, 0, -1);
        log.info("set value success, value is: [{}]", setValue);
        Long increment = operations.increment(key, 1);
        log.info("increase value success, value is: [{}]", increment);
    }

    @Test
    public void testStringRedisTemplate() {
        String key = "stringRedisTemplate_key";
        String value = "1";
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(key, value);
        String setValue = operations.get(key);
        log.info("set value success, value is: [{}]", setValue);
        Long increment = operations.increment(key);
        log.info("increase value success, value is: [{}]", increment);
    }


}

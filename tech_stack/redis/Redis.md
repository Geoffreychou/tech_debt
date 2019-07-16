# Redis

## RedisTemplate 踩坑

最近，和 RedisTemplate 打交道比较多，RedisTemplate 是 Spring 提供的一个很方便的 Redis 操作 API。它封装了 Redis 客户端一系列的操作的。但是，在使用 RedisTemplate 的 increment 操作时，遇到了一个小坑。下面，就分析一下这个问题。



### 问题描述

我的需求是在 Redis 中维护一个递增的计数器，一瞬间想到的就是 Redis 的 INCR & INCRBY。因为项目中集成的是 Spring 的 RedisTemplate，简单看了一下文档，然后就上手操作，一气呵成，便有了以下的代码。

```java
@Resource(name = "redisTemplate")
private RedisTemplate redisTemplate;

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
```

兴高采烈的运行一下，结果，竟然运行失败，日志显示，我们增加的值不是一个 Integer 或者已经超出 Integer 的范围。

> 2019-06-30 15:00:02.415  INFO 16580 --- [           main] io.lettuce.core.EpollProvider            : Starting without optional epoll library
> 2019-06-30 15:00:02.417  INFO 16580 --- [           main] io.lettuce.core.KqueueProvider           : Starting without optional kqueue library
> 2019-06-30 15:00:06.334  INFO 16580 --- [           main] xin.zero2one.template.RedisTemplateTest  : set value success, value is: [1]
>
> org.springframework.data.redis.RedisSystemException: Error in execution; nested exception is io.lettuce.core.RedisCommandExecutionException: ERR value is not an integer or out of range

这就很难受了，因为赶时间，就去谷歌了一把，大部分给的建议是需要将 RedisTemplate 的 value 序列化器改为 StringRedisSerializer，但是项目中用的一直是默认的 JdkSerializationRedisSerializer，改了这个，之前存在 Redis 的数据反序列化就会有问题。

然后，为了方便，就自己重新注入了 StringRedisTemplate，代码如下：

```java
@Resource(name = "stringRedisTemplate")
private StringRedisTemplate stringRedisTemplate;

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
```

运行一遍，一把过，很开心，日志如下：

> 2019-06-30 16:01:09.249  INFO 2560 --- [           main] io.lettuce.core.EpollProvider            : Starting without optional epoll library
> 2019-06-30 16:01:09.251  INFO 2560 --- [           main] io.lettuce.core.KqueueProvider           : Starting without optional kqueue library
> 2019-06-30 16:01:10.813  INFO 2560 --- [           main] xin.zero2one.template.RedisTemplateTest  : set value success, value is: [1]
> 2019-06-30 16:01:10.816  INFO 2560 --- [           main] xin.zero2one.template.RedisTemplateTest  : increase value success, value is: [2]

然后就敲敲敲，提交代码，因为很顺利，就没有仔细去研究第一种方法失败的具体原因，反正知道主要原因就是value 序列化与反序列化的原因。

提交完代码，让同事帮忙 review，然后同事跟我讲，之前代码中也有用到 RedisTemplate 的 increment API，并没有出现问题。

这就尴尬了，为什么我这边会遇到问题呢？然后看了一下之前的用法，代码如下：

```java
@Test
public void testRedisTemplate2() {
    String key = "redisTemplate_key_2";
    ValueOperations valueOperations = redisTemplate.opsForValue();
    valueOperations.increment(key, 1);
    BoundValueOperations boundValueOperations = redisTemplate.boundValueOps(key);
    Object setValue = boundValueOperations.get(0, -1);
    log.info("set value success, value is: [{}]", setValue);
    Long increment = valueOperations.increment(key, 1);
    log.info("increase value success, value is: [{}]", increment);
}
```

可以看到，和我的做法不同的有2处：

1. 因为是从 0 开始加 1，直接使用了 increment 方法，并没有设置初始值。
2. 获取值的时候，获取的值使用的是 get(key, start, end) 这种方式。



### 问题分析

先分析第一点，increment 方法。

追踪源码，可以看到，increment 方法会调用到如下方法。

```java
@Override
public Long incrBy(byte[] key, long value) {

    Assert.notNull(key, "Key must not be null!");

    try {
        if (isPipelined()) {
            pipeline(connection.newLettuceResult(getAsyncConnection().incrby(key, value)));
            return null;
        }
        if (isQueueing()) {
            transaction(connection.newLettuceResult(getAsyncConnection().incrby(key, value)));
            return null;
        }
        return getConnection().incrby(key, value);
    } catch (Exception ex) {
        throw convertLettuceAccessException(ex);
    }
}
```

然后，通过 getConnection() 方法，拿到 RedisStringCommands 的代理类，执行incrby 方法会执行代理类的 invoke 方法。

```java
public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (args == null) {
        args = NO_ARGS;
    }
    if (args.length == 0 && method.getName().equals("hashCode")) {
        return hashCode();
    }
    if (args.length == 1 && method.getName().equals("equals") && method.getParameterTypes()[0] == Object.class) {
        Object arg = args[0];
        if (arg == null) {
            return false;
        }
        if (proxy == arg) {
            return true;
        }
        return isProxyOfSameInterfaces(arg, proxy.getClass()) && equals(Proxy.getInvocationHandler(arg));
    }
    if (args.length == 0 && method.getName().equals("toString")) {
        return toString();
    }
    return handleInvocation(proxy, method, args);
}
```

实际执行的方法在 handleInvocation 中，上面的判断条件是处理 hashCode | equals | toString 方法。

```java
protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {

    try {
        Method targetMethod = this.translator.get(method);
        Object result = targetMethod.invoke(asyncApi, args);

        if (result instanceof RedisFuture<?>) {

            RedisFuture<?> command = (RedisFuture<?>) result;

            if (isNonTxControlMethod(method.getName()) && isTransactionActive(connection)) {
                return null;
            }

            long timeout = getTimeoutNs(command);

            return LettuceFutures.awaitOrCancel(command, timeout, TimeUnit.NANOSECONDS);
        }

        return result;
    } catch (InvocationTargetException e) {
        throw e.getTargetException();
    }
}
```

然后执行 targetMethod.invoke(asyncApi, args)，最终会执行到 dispatch 方法，可以看到，它将参数封装成 RedisCommand。

```java
public <T> RedisCommand<K, V, T> dispatch(RedisCommand<K, V, T> command) {

    RedisCommand<K, V, T> toSend = preProcessCommand(command);

    try {
        return super.dispatch(toSend);
    } finally {
        if (command.getType().name().equals(MULTI.name())) {
            multi = (multi == null ? new MultiOutput<>(codec) : multi);
        }
    }
}
```

参数格式如下：

![1561886180777](E:\tech_stack_parent\tech_stack\redis\imgs\1561886180777.png)

可以看到其中包含了我们操作的 type 以及我们传递的2 个参数。最终，通过 http 调用，将请求发送到 Redis 执行（lettuce 底层封装了 Netty）。需要关注的是，singularArguments 里的第二个参数，及我们递增的值，和我们传递的值一模一样，没有被序列化。

我们查看一下 redis 中的数据，确实并没有被序列化。

![1561886739175](E:\tech_stack_parent\tech_stack\redis\imgs\1561886739175.png)



我们可以看一下，如果是 set(key, value)，其 value 存到 Redis 中会是怎样的。

![1561886904372](E:\tech_stack_parent\tech_stack\redis\imgs\1561886904372.png)

可以看到，“1” 被序列化成一堆正常人看不懂的字符。

其实，到这里，我们应该差不多能够理解，执行 set(key, value) 后，为什么执行 increment 会失败，因为，Redis 中存的值，本身就不是一个数字，肯定没法递增。



接着分析第二点，获取值时，为什么需要使用 get(key, start, end) ，而不能直接 get(key) 呢？

看到 get(key, start, end) 的实现，其实就很明白了。

```java
@Override
public String get(K key, long start, long end) {
    byte[] rawKey = rawKey(key);
    byte[] rawReturn = execute(connection -> connection.getRange(rawKey, start, end), true);

    return deserializeString(rawReturn);
}

String deserializeString(byte[] value) {
    return (String) stringSerializer().deserialize(value);
}
```

该方法得到返回的字节，使用的是 StringRedisSerializer 进行反序列化的。

再看一下 get(key) 方法，使用的是定义的 value 序列化工具进行反序列化。因为我们在第一步，存值的时候，并没有使用 value 的序列化工具进行序列化，取值的时候使用反序列化肯定会出错。

```java
public final V doInRedis(RedisConnection connection) {
    byte[] result = inRedis(rawKey(key), connection);
    return deserializeValue(result);
}

V deserializeValue(byte[] value) {
    if (valueSerializer() == null) {
        return (V) value;
    }
    return (V) valueSerializer().deserialize(value);
}

RedisSerializer valueSerializer() {
    return template.getValueSerializer();
}
```



我们可以看一下 StringRedisSerializer 的序列化和反序列化，只是简单进行字符串和字节的转换。

```java
@Override
public String deserialize(@Nullable byte[] bytes) {
    return (bytes == null ? null : new String(bytes, charset));
}

/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.serializer.RedisSerializer#serialize(java.lang.Object)
	 */
@Override
public byte[] serialize(@Nullable String string) {
    return (string == null ? null : string.getBytes(charset));
}
```



### 总结

#### RedisTemplate  的错误用法

当我使用 RedisTemplate 操作时，在 set 值时，存到 Redis 中的 value  “1” 已经变成 "\xac\xed\x00\x05t\x00\x011" 这样一串字符，所以在执行 increment 方法时，肯定会是失败的。

```java
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
```



#### StringRedisTemplate  的用法

当我使用 StringRedisTemplate 操作时，由于 StringRedisTemplate 默认使用的是 StringRedisSerializer 序列化工具，存入到 Redis 中和我们代码中设置的值是一样的。所以执行 increment 方法时，能够正常的递增，执行 get(key)  时，使用 StringRedisSerializer  反序列化，也能够成功。

```java
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
```



![1561888234198](E:\tech_stack_parent\tech_stack\redis\imgs\1561888234198.png)



StringRedisTemplate 的构造方法：

```java
public StringRedisTemplate() {
    setKeySerializer(RedisSerializer.string());
    setValueSerializer(RedisSerializer.string());
    setHashKeySerializer(RedisSerializer.string());
    setHashValueSerializer(RedisSerializer.string());
}
```



#### RedisTemplate  的正确用法

当我们只用 RedisTemplate 按照如下方式操作时，increment 方法存入到 Redis 中的值并没有被序列化，和原值一样（为了和 StringRedisTemplate 对比，我们可以认为，这一步执行了 StringRedisSerializer  的序列化操作），然后我们 get(key, start, end) 时，拿到 value 的字节，然后使用 StringRedisSerializer  进行反序列化，肯定是可以反序列化成功的。

```java
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
```



#### 综述

通过分析，可以看到，问题的本质还是序列化和反序列化的问题。

当我们使用第三方提供的 API 时，对于自己不熟悉的，一定要好好研究其实现原理，使用场景以及用法，这样才能避开一些难以定位的错误。



原始代码

```java
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
```
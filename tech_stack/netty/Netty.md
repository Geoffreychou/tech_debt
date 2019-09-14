# Netty



## 实现简单的 RPC 调用

### 定义接口

首先定义一个接口 `RpcService` ，作为服务端实现类的接口和客户端远程调用的接口。

```java
package xin.zero2one.netty.rpc.service;

public interface RpcService {

    /**
     * say hi
     *
     * @param msg
     * @return
     */
    String sayHi(String msg);

    /**
     * say goodBye
     *
     * @param msg
     * @return
     */
    String sayBye(String msg);

}
```



### 定义协议

如 http 等协议一样，我们可以自定义我们的 RPC 调用协议 -- `RpcProtocol`。这边，我简单定义了必须的协议参数。

```java
package xin.zero2one.netty.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcProtocol implements Serializable {

    /**
     * 调用的接口名称
     */
    private String className;

    /**
     * 调用的方法名称
     */
    private String methodName;

    /**
     * 调用方法的参数类型
     */
    private Class<?>[] parameterTypes;

    /**
     * 调用方法的实参
     */
    private Object[] parameters;

}
```





### 服务端

#### 实现接口

服务端实现 `RpcService` 接口。

```java
package xin.zero2one.netty.rpc.server.provider;

import lombok.extern.slf4j.Slf4j;
import xin.zero2one.netty.rpc.service.RpcService;

@Slf4j
public class RpcServiceImpl implements RpcService {

    @Override
    public String sayHi(String msg) {
        log.info("sayHi: {}", msg);
        return "hi, client!";
    }

    @Override
    public String sayBye(String msg) {
        log.info("sayBye: {}", msg);
        return "Bye, client!";
    }
}
```



#### 定义 ChannelHandler

`ChannelHandler` 用来处理服务端接收的客户端的请求。这里，我们定义一个类 `ServerInvoker`，继承 `ChannelInboundHandlerAdapter` 类， `ChannelInboundHandlerAdapter` 是 Netty 基于 `ChannelHandler`  封装完备的实现类。这样，我们就不用将接口的每个方法实现一遍了。



##### 初始化

`ServerInvoker` 初始化过程中主要做两件事，分别为扫描类和加载类。



###### 扫描类

`xin.zero2one.netty.rpc.server.ServerInvoker#scanClasses` 方法其主要作用就是扫描指定的包路径下的类，并放入 Set 集合中。这里，我们只需要扫描定义的远程调用的接口的实现类即可。



###### 注册类

`xin.zero2one.netty.rpc.server.ServerInvoker#registerClasses` 方法其主要作用是将扫描得到的实现类加载，并保存到 classInstanceMap 中，map 的 key 为实现类的接口的名称。



##### 重写 channelRead 方法

`xin.zero2one.netty.rpc.server.ServerInvoker#channelRead` 方法是当服务端接收客户端请求，读取客户端请求的数据时的回调方法（这里先不解释回调的实现原理，后面的文章会慢慢分析）。

该方法有 2 个参数，分别为 `ChannelHandlerContext ctx` 和 `Object msg`。 ctx 是 channelHandler 的上下文，可用于和 `ChannelPipeline` 中其他的 handler 进行交互。 msg 即客户端传输过来的数据，即我们根据 `RpcProtocol` 定义的数据。

首先，我们在初始化过的 classInstanceMap 中根据接口名找到对应的实现类，然后通过根据方法名和方法参数类型数组找到对应的 方法，最后通过反射执行方法，并将执行的返回值写入到  ctx 中，并返回给客户端。



```java
package xin.zero2one.netty.rpc.server;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import xin.zero2one.netty.rpc.protocol.RpcProtocol;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ServerInvoker extends ChannelInboundHandlerAdapter {

    private static Map<String,Object> classInstanceMap = new HashMap<>(16);

    private static Set<String> classNames = new HashSet<>(16);

    private static String PACKAGE_PATH = "xin.zero2one.netty.rpc.server.provider";

    static {
        scanClasses(PACKAGE_PATH);
        registerClasses();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof RpcProtocol)) {
            super.channelRead(ctx, msg);
        }
        Object result = new Object();
        RpcProtocol requestMsg = (RpcProtocol) msg;
        if (classInstanceMap.containsKey(requestMsg.getClassName())) {
            Object instance = classInstanceMap.get(requestMsg.getClassName());
            Method method = instance.getClass().getMethod(requestMsg.getMethodName(), requestMsg.getParameterTypes());
            if (null == method) {
                log.error("method not found by name and parameterTypes");
                throw new Exception("method not found by name and parameterTypes");
            }
            result = method.invoke(instance, requestMsg.getParameters());
        }
        ctx.writeAndFlush(result);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
        ctx.close();
    }


    private static void scanClasses(String packageName) {
        URL resources = ServerInvoker.class.getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        try {
            Path path = Paths.get(resources.toURI());
            File file = path.toFile();
            File[] files = file.listFiles();
            for(File childFile : files) {
                if (childFile.isDirectory()) {
                    scanClasses(packageName + "." + childFile.getName());
                } else {
                    classNames.add(packageName + "." + childFile.getName().replace(".class", "").trim());
                }
            }
        } catch (URISyntaxException e) {
            log.error("ServerInvoker init error", e);
            throw new RuntimeException(e);
        }
    }

    private static void registerClasses() {
        if (classNames.isEmpty()) {
            return;
        }
        for(String className : classNames) {
            log.info("load class: {}", className);
            try {
                Class<?> clazz = Class.forName(className);
                Class<?>[] interfaces = clazz.getInterfaces();
                if (null == interfaces || interfaces.length != 1) {
                    throw new RuntimeException("{} has no interface or it's interface has more than 1 implements");
                }
                classInstanceMap.put(interfaces[0].getName(), clazz.newInstance());
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }

        }
    }
}

```



#### 定义 ChannelInitializer

`ChannelInitializer` 是一个特殊的 `ChannelInboundHandler` ，其为我们提供了一个初始化 channel 的简单方法。我们实现其 `io.netty.channel.ChannelInitializer#initChannel(C)` 方法，当 channel 被注册时，会回调该方法。该方法里，我们在 pipeline 中添加了一系列的编解码处理器，同时，添加了我们自定义的 ChannelHandler --  `ServerInvoker` 。

```java
package xin.zero2one.netty.rpc.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new ObjectEncoder());
        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
        pipeline.addLast(new ServerInvoker());
    }
}
```



#### 定义 RpcServer

`RpcServer` 即我们的 Netty 服务，用于监听我们指定的端口的请求，其写法几乎是固定。

第一步，定义 bossGrop & workerGroup。 简单的说，bossGroup 用来接收客户端的请求，workerGroup 用来处理具体的请求。

第二步，封装 ServerBootstrap。ServerBootstrap 是装配服务端的引导类。通过 ServerBootstrap，我们很方便的定义 parentGroup & childGroup ，定义 channel， 定义 handler，定义 option 等。

第三步，绑定端口，启动服务。



```java
package xin.zero2one.netty.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcServer {

    /**
     * port
     */
    private int port;

    public RpcServer(int port) {
        this.port = port;
    }

    public void start() {
        int processorCount = Runtime.getRuntime().availableProcessors();
        EventLoopGroup bossGroup = new NioEventLoopGroup(processorCount << 1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(processorCount << 1);
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = server.bind(port).sync();
            log.info("RPC server started, listen at port: {}", this.port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("RPC server fail to start, error msg: {}", e.getMessage(), e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new RpcServer(8888).start();
    }

}
```



### 客户端

#### 定义 ChannelHandler

和服务端一样，客户端也需要定义 `定义 ChannelHandler` 用来处理与服务端交互的数据。

这里，我定义了一个成员变量 response，其作用是用来保存服务端返回的数据，方便我们获取和处理。

同时，我们重写了 `xin.zero2one.netty.rpc.client.ClientInvokerHandler#channelRead`方法，将获取到的服务端返回的数据赋值给 response。

```java
package xin.zero2one.netty.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientInvokerHandler extends ChannelInboundHandlerAdapter {

    private Object response;

    public Object getResponse() {
        return this.response;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.response = msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
        ctx.close();
    }
}
```



#### 定义 ChannelInitializer

和服务端一样，相同的是编解码处理器，不同的是，这里使用了客户端定义的 channelHandler。

```java
package xin.zero2one.netty.rpc.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private ChannelHandler clientInvokerHandler;

    public ClientChannelInitializer(ChannelHandler clientInvokerHandler) {
        this.clientInvokerHandler = clientInvokerHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new ObjectEncoder());
        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
        pipeline.addLast(this.clientInvokerHandler);
    }
}
```



#### 定义代理类

定义 `RpcProxy` ，实现 `InvocationHandler` 。这是一个典型的动态代理类的 handler，用来代理我们指定的接口，即 RpcService。

我们定义了两个成员变量，一个是 clazz ，即我们代理的类类型， rpcClient，即 rpc 客户端，在下一步中会定义该类。

其 invoke 方法按照我们定义的协议，将调用参数封装为 `RpcProtocol` ，然后 rpcClient 将封装好的数据发送到服务端。



```java
package xin.zero2one.netty.rpc.client.proxy;

import xin.zero2one.netty.rpc.client.RpcClient;
import xin.zero2one.netty.rpc.protocol.RpcProtocol;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RpcProxy implements InvocationHandler {

    private Class<?> clazz;

    private RpcClient rpcClient;

    public RpcProxy(Class<?> clazz, RpcClient rpcClient) {
        this.clazz = clazz;
        this.rpcClient = rpcClient;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invoke = rpcClient.invoke(buildRequestMsg(method, args));
        return invoke;
    }

    private RpcProtocol buildRequestMsg(Method method, Object[] args) {
        RpcProtocol requestMsg = new RpcProtocol();
        requestMsg.setClassName(this.clazz.getName());
        requestMsg.setMethodName(method.getName());
        requestMsg.setParameterTypes(method.getParameterTypes());
        requestMsg.setParameters(args);
        return requestMsg;
    }
}
```



####  定义客户端

定义 `RpcClient` ，其主要用来做两件事。

第一，获取动态代理对象。

第二，将封装好的数据发送至服务端。



##### 获取代理类

获取代理类就是经典动态代理的方式。通过 Proxy 的 newProxyInstance 方法实现。当调用接口的方法时，实际调用的是 `java.lang.reflect.InvocationHandler#invoke`  方法。



##### 发送数据

其实现方式为创建客户端连接，将数据发送到服务端。其写法几乎也是固定。

第一步，定义 eventLoopGroup。 用于处理客户端的请求。

第二步，封装 Bootstrap。Bootstrap 是装配客户端的引导类。通过 Bootstrap，我们很方便的定义 group ，定义 channel， 定义 handler，定义 option 等。

第三步，创建连接，连接到服务端的 ip 和 port，并将数据发送的服务端。

第四步，通过自定义的 channelHandler 获取到服务端的返回数据。



```java
package xin.zero2one.netty.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import xin.zero2one.netty.rpc.client.proxy.RpcProxy;

import java.lang.reflect.Proxy;

@Slf4j
public class RpcClient {

    private String ip;
    private int port;

    public RpcClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public <T> T getRemoteInstance(Class<T> clazz) {
        if (!clazz.isInterface()) {
            throw new RuntimeException("clazz is not interface");
        }
        RpcProxy rpcProxy = new RpcProxy(clazz, this);

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, rpcProxy);
    }


    public Object invoke(Object msg) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        ClientInvokerHandler clientInvokerHandler = new ClientInvokerHandler();
        try {
            Bootstrap client = new Bootstrap();
            client.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ClientChannelInitializer(clientInvokerHandler));
            ChannelFuture channelFuture = client.connect(this.ip, this.port).sync();
            channelFuture.channel().writeAndFlush(msg).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("invoke remote error" , e);
            throw new RuntimeException(e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
        return clientInvokerHandler.getResponse();
    }
}
```



### 测试

至此，我们基于 Netty 的一个简单的 RPC 服务就写好了。

下面，我们简单的测试一下我们 RPC 服务。

首先，启动服务端，监听 8888 端口。



```java
20:51:57.082 [main] INFO xin.zero2one.netty.rpc.server.RpcServer - RPC server started, listen at port: 8888
```

然后，写一个客户端的测试类。首先，初始化客户端，然后，获取远程服务的代理类，最后，调用代理类的两个方法。代码如下。



```java
package xin.zero2one.netty.rpc.client;

import lombok.extern.slf4j.Slf4j;
import xin.zero2one.netty.rpc.service.RpcService;

@Slf4j
public class RpcTest {

    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient("127.0.0.1", 8888);
        RpcService rpcService = rpcClient.getRemoteInstance(RpcService.class);
        log.info("sayHi, return: {}", rpcService.sayHi("hi. server"));
        log.info("sayBye, return: {}", rpcService.sayBye("Bye. server"));
    }

}

```



客户端打印日志如下：

```java
20:56:58.015 [main] INFO xin.zero2one.netty.rpc.client.RpcTest - sayHi, return: hi, client!
20:56:58.049 [main] INFO xin.zero2one.netty.rpc.client.RpcTest - sayBye, return: Bye, client!
```



服务端打印日志如下：

```java
20:56:58.003 [nioEventLoopGroup-3-3] INFO xin.zero2one.netty.rpc.server.provider.RpcServiceImpl - sayHi: hi. server
20:56:58.048 [nioEventLoopGroup-3-4] INFO xin.zero2one.netty.rpc.server.provider.RpcServiceImpl - sayBye: Bye. server
```



可以看到，客户端成功的调用了服务端的服务，并且，获取到了服务端的返回值。
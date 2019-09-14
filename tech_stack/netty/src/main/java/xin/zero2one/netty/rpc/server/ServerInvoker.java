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

/**
 * @author zhoujundong
 * @data 9/14/2019
 * @description invoker
 */
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

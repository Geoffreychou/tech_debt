package xin.zero2one.netty.rpc.client.proxy;

import xin.zero2one.netty.rpc.client.RpcClient;
import xin.zero2one.netty.rpc.protocol.RpcProtocol;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author zhoujundong
 * @data 9/14/2019
 * @description remote proxy
 */
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

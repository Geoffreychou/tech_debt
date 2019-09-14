package xin.zero2one.netty.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhoujundong
 * @data 9/14/2019
 * @description protocol
 */
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

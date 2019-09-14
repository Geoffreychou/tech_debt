package xin.zero2one.netty.rpc.service;

/**
 * @author zhoujundong
 * @data 9/14/2019
 * @description provider interface
 */
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

package xin.zero2one.netty.rpc.client;

import lombok.extern.slf4j.Slf4j;
import xin.zero2one.netty.rpc.service.RpcService;

/**
 * @author zhoujundong
 * @data 9/14/2019
 * @description test
 */
@Slf4j
public class RpcTest {

    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient("127.0.0.1", 8888);
        RpcService rpcService = rpcClient.getRemoteInstance(RpcService.class);
        log.info("sayHi, return: {}", rpcService.sayHi("hi. server"));
        log.info("sayBye, return: {}", rpcService.sayBye("Bye. server"));
    }

}

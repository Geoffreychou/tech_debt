package xin.zero2one.netty.rpc.server.provider;

import lombok.extern.slf4j.Slf4j;
import xin.zero2one.netty.rpc.service.RpcService;

/**
 * @author zhoujundong
 * @data 9/14/2019
 * @description provider impl
 */
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

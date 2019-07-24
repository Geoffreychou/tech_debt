package xin.zero2one.service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhoujundong
 * @data 7/24/2019
 * @description TODO
 */
@Slf4j
public class CommonHelloWorldService implements HelloWorldService {

    @Override
    public void sayHello(String msg) {
        log.info("{} say hello msg: {}", this.getClass().getName(), msg);
    }
}

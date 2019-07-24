package xin.zero2one.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xin.zero2one.service.CommonHelloWorldService;
import xin.zero2one.service.HelloWorldService;

/**
 * @author zhoujundong
 * @data 7/24/2019
 * @description TODO
 */
@Configuration
public class HelloWorldConfiguration {

    @Bean
    public HelloWorldService commonHelloWorldService() {
        return new CommonHelloWorldService();
    }

}

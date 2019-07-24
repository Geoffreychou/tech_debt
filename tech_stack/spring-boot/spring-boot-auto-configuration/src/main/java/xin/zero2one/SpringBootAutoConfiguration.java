package xin.zero2one;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import xin.zero2one.service.HelloWorldService;

/**
 * @author zhoujundong
 * @data 7/23/2019
 * @description TODO
 */
@EnableAutoConfiguration
public class SpringBootAutoConfiguration {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SpringBootAutoConfiguration.class, args);

        HelloWorldService helloWorldService = run.getBean(HelloWorldService.class);
        helloWorldService.sayHello("hi!");
    }

}

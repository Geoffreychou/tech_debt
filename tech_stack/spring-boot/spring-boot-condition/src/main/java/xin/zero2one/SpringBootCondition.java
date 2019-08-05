package xin.zero2one;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author zhoujundong
 * @data 8/1/2019
 * @description spring boot condition
 */
@SpringBootApplication
@Slf4j
public class SpringBootCondition {

    public static void main(String[] args) {
//        System.setProperty("spring.config.name","zero2one-config");
        ConfigurableApplicationContext run = SpringApplication.run(SpringBootCondition.class, args);
        ConfigurableEnvironment environment = run.getEnvironment();

        String common = environment.getProperty("xin.zero2one.configuration.common");
        String dev = environment.getProperty("xin.zero2one.configuration.dev");
        String test = environment.getProperty("xin.zero2one.configuration.test");
        String prod = environment.getProperty("xin.zero2one.configuration.prod");
        String zero2one = environment.getProperty("xin.zero2one.configuration.zero2one");

        log.info("properties: common = {}, dev = {}, test = {}, prod = {}, zero2one = {}", common, dev, test, prod, zero2one);
    }


}

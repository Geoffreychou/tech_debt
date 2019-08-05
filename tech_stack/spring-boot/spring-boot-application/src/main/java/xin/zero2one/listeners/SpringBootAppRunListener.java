package xin.zero2one.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author zhoujundong
 * @data 8/2/2019
 * @description TODO
 */
@Slf4j
public class SpringBootAppRunListener implements SpringApplicationRunListener{

    public SpringBootAppRunListener(SpringApplication springApplication, String... args) {
        log.info("init my SpringBootApp, springApplication: [{}], args: []", springApplication, args);
    }

    @Override
    public void starting() {
        log.info("my SpringBootApp is staring");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        log.info("my SpringBootApp environmentPrepared");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.info("my SpringBootApp contextPrepared");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        log.info("my SpringBootApp contextLoaded");
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        log.info("my SpringBootApp is started");
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        log.info("my SpringBootApp is running");
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        log.info("my SpringBootApp is failed");
    }
}

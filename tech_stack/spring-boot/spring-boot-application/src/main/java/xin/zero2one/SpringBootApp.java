package xin.zero2one;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.ResolvableType;

/**
 * @author zhoujundong
 * @data 8/2/2019
 * @description TODO
 */
@SpringBootApplication
public class SpringBootApp {

    public static void main(String[] args) {

        ResolvableType resolvableType = ResolvableType.forType(SmartApplicationListener.class);
//        ResolvableType resolvableType = ResolvableType.forType(ConfigFileApplicationListener.class);
        ResolvableType as = resolvableType.as(ApplicationListener.class);

        SpringApplication.run(SpringBootApp.class, args);
    }

}
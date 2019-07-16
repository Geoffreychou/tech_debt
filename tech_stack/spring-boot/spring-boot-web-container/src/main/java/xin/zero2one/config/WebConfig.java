package xin.zero2one.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author ZJD
 * @date 2019/4/21
 */
@Slf4j
@Configuration
public class WebConfig {
    @Bean
    public RouterFunction<ServerResponse> helloWorldRoute(){
        return route(GET("/helloWorld"), request -> ok().body(Mono.just("helloWorld"), String.class));
    }

    @Bean
    public ApplicationRunner runner(WebServerApplicationContext context, BeanFactory beanFactory){
        return args -> {
            log.info("args is " + args);
            log.info("ApplicationRunner: WebServer is {}", context.getWebServer().getClass().getName());
            log.info("ApplicationRunner: helloWorld bean class is {}", beanFactory.getBean("helloWorld").getClass().getName());
//            log.info("ApplicationRunner: webConfig bean class is {}", beanFactory.getBean("webConfig").getClass().getName());
        };
    }

    @EventListener
    public void onWebServerInit(WebServerInitializedEvent event){
        log.info("WebServerInitializedEvent: WebServer is {}", event.getWebServer().getClass().getName());
    }
}

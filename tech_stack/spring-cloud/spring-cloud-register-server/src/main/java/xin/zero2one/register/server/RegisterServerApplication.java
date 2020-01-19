package xin.zero2one.register.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author zhoujundong
 * @data 1/15/2020
 * @description
 */
@SpringBootApplication
@EnableEurekaServer
public class RegisterServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegisterServerApplication.class, args);
    }
}

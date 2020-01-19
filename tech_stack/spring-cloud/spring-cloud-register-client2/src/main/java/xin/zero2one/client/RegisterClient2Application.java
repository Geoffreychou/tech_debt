package xin.zero2one.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author zhoujundong
 * @data 1/16/2020
 * @description
 */

@SpringBootApplication
@EnableEurekaClient
public class RegisterClient2Application {

    public static void main(String[] args) {
        SpringApplication.run(RegisterClient2Application.class, args);
    }
}

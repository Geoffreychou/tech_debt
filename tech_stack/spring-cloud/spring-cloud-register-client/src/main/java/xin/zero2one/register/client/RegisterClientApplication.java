package xin.zero2one.register.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author zhoujundong
 * @data 1/16/2020
 * @description
 */

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class RegisterClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegisterClientApplication.class, args);
    }
}

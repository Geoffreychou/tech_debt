package xin.zero2one;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

//@EnableAutoConfiguration
@SpringBootApplication
public class SpringBootFirstApp {
    public static void main( String[] args ) {
        ConfigurableApplicationContext run = SpringApplication.run(SpringBootFirstApp.class, args);
        Object testBean = run.getBeanFactory().getBean("testBean");
        System.out.println(testBean);

    }

    public void get() {
        return;
    }

    @Bean
    public String hello() {
        return "hello";
    }
}

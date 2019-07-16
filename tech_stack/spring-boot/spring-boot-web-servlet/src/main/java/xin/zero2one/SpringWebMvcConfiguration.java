package xin.zero2one;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import xin.zero2one.controller.HelloWorldController;

/**
 * @author zhoujundong
 * @data 6/17/2019
 * @description TODO
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "xin.zero2one.controller")
public class SpringWebMvcConfiguration {
}

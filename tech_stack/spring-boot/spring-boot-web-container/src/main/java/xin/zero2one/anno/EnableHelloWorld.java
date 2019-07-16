package xin.zero2one.anno;

import org.springframework.context.annotation.Import;
import xin.zero2one.config.HelloWorldConfiguration;

import java.lang.annotation.*;

/**
 * @author ZJD
 * @date 2019/6/3
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(HelloWorldConfiguration.class)
public @interface EnableHelloWorld {
}

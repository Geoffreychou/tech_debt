package xin.zero2one.anno;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @author zhoujundong
 * @data 5/28/2019
 * @description TODO
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface ServiceChild {

    @AliasFor(value = "value", annotation = Service.class)
    String childValue() default "";

    @AliasFor("childValue")
    String value() default "";

}

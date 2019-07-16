package xin.zero2one.anno;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author ZJD
 * @date 2019/6/2
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnnoThree {

    @AliasFor(attribute = "aliasTwo")
    String aliasOne() default "";

    @AliasFor(attribute = "aliasOne")
    String aliasTwo() default "";

    String attrOne() default "attrTwo";
}

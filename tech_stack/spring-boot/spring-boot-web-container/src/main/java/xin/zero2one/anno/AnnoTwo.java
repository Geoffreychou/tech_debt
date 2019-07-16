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
@AnnoThree
public @interface AnnoTwo {

    @AliasFor(attribute = "aliasOne", annotation = AnnoThree.class)
    String aliasThree() default "";

    String attrOne() default "attrTwo";

    String nameTwo() default "nameTwo";
}

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
@AnnoTwo
public @interface AnnoOne {

    @AliasFor(attribute = "aliasThree", annotation = AnnoTwo.class)
    String aliasFour() default "aliasOne";

    String attrOne() default "attrOne";

    @AliasFor(attribute = "nameTwo", annotation = AnnoTwo.class)
    String nameOne() default "nameOne";

}

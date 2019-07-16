package xin.zero2one.anno;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * @author zhoujundong
 * @data 5/31/2019
 * @description TODO
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Transactional
@Service(value = "transactionalService")
public @interface TransactionalService {

//    @AliasFor(value = "value", annotation = TransactionalService.class)
//    String name() default "txManager";
//
//    @AliasFor(value = "name", annotation = TransactionalService.class)
//    String value() default "txManager";

    @AliasFor(attribute = "transactionManager", annotation = Transactional.class)
    String manager() default "txManager";
}

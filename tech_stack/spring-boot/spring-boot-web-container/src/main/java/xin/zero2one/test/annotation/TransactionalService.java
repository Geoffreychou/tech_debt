//package xin.zero2one.test.annotation;
//
//import org.springframework.core.annotation.AliasFor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.lang.annotation.*;
//
///**
// * @author ZJD
// * @date 2019/4/23
// */
//@Target({ElementType.TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//@Documented
//@Transactional
//@Service(value = "transactionalService")
//public @interface TransactionalService {
//
//    @AliasFor(attribute = "value")
//    String name() default "";
//
//    @AliasFor(attribute = "name")
//    String value() default "";
//
//    @AliasFor(attribute = "transactionManager", annotation = Transactional.class)
//    String manager() default "txManager";
//}

//package xin.zero2one.test.annotation.reflect;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.type.AnnotationMetadata;
//import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
//import org.springframework.core.type.classreading.MetadataReader;
//import org.springframework.core.type.classreading.MetadataReaderFactory;
//import org.springframework.util.ReflectionUtils;
//import xin.zero2one.test.annotation.TransactionalService;
//
//import java.io.IOException;
//import java.lang.annotation.Annotation;
//import java.lang.annotation.Target;
//import java.lang.reflect.AnnotatedElement;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
///**
// * @author ZJD
// * @date 2019/4/23
// */
//@Slf4j
//@TransactionalService(name = "transactionalService", value = "666")
//public class TransactionalServiceAnnotationReflectBootstrap {
//
//    public static void main(String[] args) {
//        AnnotatedElement annotatedElement = TransactionalServiceAnnotationReflectBootstrap.class;
//        TransactionalService annotation = annotatedElement.getAnnotation(TransactionalService.class);
//
//
//
//
//    }
//
//    private static Set<Annotation> getAllMetaAnnotations(Annotation annotation){
//        Annotation[] metaAnnotations = annotation.annotationType().getAnnotations();
//        Set<Annotation> metaAnnotationSet = Stream.of(metaAnnotations)
//                .filter(metaAnnotation -> Target.class.getPackage().equals(metaAnnotation.annotationType().getPackage()))
//                .collect(Collectors.toSet());
//        Set<Annotation> collect = metaAnnotationSet.stream()
//                .map(TransactionalServiceAnnotationReflectBootstrap::getAllMetaAnnotations)
//                .collect(HashSet::new, Set::addAll, Set::addAll);
//        metaAnnotationSet.addAll(collect);
//        return metaAnnotationSet;
//    }
//
//    private void printAnnotation(Annotation annotation){
//        ReflectionUtils.doWithMethods(TransactionalService.class,
//                method -> log.info("@TransactionalService.{} = {}", method.getName(), ReflectionUtils.invokeMethod(method, annotation)),
//                method -> !method.getDeclaringClass().equals(Annotation.class));
//    }
//
//}

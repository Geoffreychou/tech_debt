//package xin.zero2one.test.annotation.composed;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.type.AnnotationMetadata;
//import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
//import org.springframework.core.type.classreading.MetadataReader;
//import org.springframework.core.type.classreading.MetadataReaderFactory;
//import xin.zero2one.test.annotation.TransactionalService;
//
//import java.io.IOException;
//import java.util.Set;
//
///**
// * @author ZJD
// * @date 2019/4/23
// */
//@Slf4j
//@TransactionalService
//public class TransactionalServiceAnnotationComposedBootstrap {
//
//    public static void main(String[] args) throws IOException {
//        String className = TransactionalServiceAnnotationComposedBootstrap.class.getName();
//        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
//        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(className);
//        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
//        annotationMetadata.getAnnotationTypes().stream().forEach(annotationType -> {
//            Set<String> metaAnnotationTypes = annotationMetadata.getMetaAnnotationTypes(annotationType);
//            log.info("{} 元标注 {}", annotationType, metaAnnotationTypes);
//
//        });
//    }
//
//}

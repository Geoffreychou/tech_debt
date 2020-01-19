package xin.zero2one;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.SimpleTransactionStatus;
import xin.zero2one.anno.*;
import xin.zero2one.bean.*;
import xin.zero2one.bean.AnnoTest;
import xin.zero2one.bean.ManagedBeanTest;
import xin.zero2one.bean.ServiceChildTest;
import xin.zero2one.condition.ConditionOnTest;
import xin.zero2one.factory.MyFactoryBean;
import xin.zero2one.profile.IProfileService;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Slf4j
@SpringBootApplication
@EnableTransactionManagement
//@ComponentScan(basePackageClasses = {AnnoTest.class})
@EnableHelloWorld
@EnableImportSelect
@EnableImportBeanDefinitionRegistrar
public class SpringBootWebContainer {

    @Component
    public class testBean{

    }

    public static void main( String[] args ) {
//        getAnnotatedAttr(AliasAndOverridesTest.class, AnnoOne.class);
//        getAnnotatedAttr(AliasAndOverridesTest.class, AnnoTwo.class);
//        getAnnotatedAttr(AliasAndOverridesTest.class, AnnoThree.class);

//        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "profile4,profile3");
//        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "profile1,profile3");


//        System.setProperty("condition", "two");
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootWebContainer.class, args);
//        Object conditionService = context.getBean("conditionService");
//        log.info("conditionService is : {}", conditionService);

//        IProfileService profileService = context.getBean(IProfileService.class);
//        profileService.sayHi("hello, world!");


//        testBean();
//        getAnnotations();

//        testAnnotations(context);
//        testEnableHelloWorldConfiguration(context);
//        testEnableImportSelectorConfiguration(context);
//        testEnableImportBeanDefinitionRegistrarConfiguration(context);


        testFactoryBean(context);

    }

    private static void testBean(ConfigurableApplicationContext context){
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        ManagedBeanTest managedBeanTest = beanFactory.getBean(ManagedBeanTest.class);
        log.info("managedBean : {}", managedBeanTest);
        ServiceChildTest serviceChildTest = beanFactory.getBean(ServiceChildTest.class);
        log.info("serviceChildTest by type : {}", serviceChildTest);

        ServiceChildTest serviceChildTestByName = (ServiceChildTest) beanFactory.getBean("serviceAliasName");
        log.info("serviceChildTest by name : {}", serviceChildTestByName);
    }

    private static void getAnnotations(){
        CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();
        MetadataReader metadataReader;
        try {
            metadataReader = cachingMetadataReaderFactory.getMetadataReader(SpringBootWebContainer.class.getName());
        } catch (IOException e) {
            log.error("get metaDataReader error", e);
            throw new RuntimeException(e);
        }
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        getAnnotations(annotationMetadata);

    }

    private static void getAnnotations(AnnotationMetadata annotationMetadata){
        Set<String> annotationTypes = new HashSet<>();
        annotationMetadata.getAnnotationTypes().forEach(type -> {
            getAnnotations(annotationMetadata, type, annotationTypes);
        });
        log.info("all annotations are: {}", annotationTypes.toString());
    }

    private static void getAnnotations(AnnotationMetadata annotationMetadata, String type, Set<String> annotationTypes){
        if (null == type){
            return;
        }
        annotationTypes.add(type);
        Set<String> metaAnnotationTypes = annotationMetadata.getMetaAnnotationTypes(type);
        if (null == metaAnnotationTypes || metaAnnotationTypes.isEmpty()){
            return;
        }
        metaAnnotationTypes.forEach(annoType -> getAnnotations(annotationMetadata, annoType, annotationTypes));
    }

    private static void testAnnotations(ConfigurableApplicationContext context){
        context.getBeansOfType(AnnoTest.class).forEach((beanName, bean) -> {
            log.info("beanName is [{}], bean is [{}]", beanName, bean);
            bean.save();
        });

    }

    private static void getAnnotatedAttr(AnnotatedElement element, Class annotation){
        AnnotationAttributes mergedAnnotationAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(element, annotation);
        printAnnotationAttrs(mergedAnnotationAttributes);
    }

    private static void printAnnotationAttrs(AnnotationAttributes annotationAttributes){
        Map<String,String> kv = new HashMap<>();
        annotationAttributes.forEach((name,value) -> kv.put(name, String.valueOf(value)));
        log.info("annotation [{}] attributes : [{}]", annotationAttributes.annotationType().getName(), kv.toString());
    }

    @Bean("txManager")
    public PlatformTransactionManager txManager(){
        return new PlatformTransactionManager() {
            @Override
            public TransactionStatus getTransaction(TransactionDefinition transactionDefinition) throws TransactionException {
                return new SimpleTransactionStatus();
            }

            @Override
            public void commit(TransactionStatus transactionStatus) throws TransactionException {
                log.info("txManager : execute commit ...");
            }

            @Override
            public void rollback(TransactionStatus transactionStatus) throws TransactionException {
                log.info("txManager : execute rollback ...");
            }
        };
    }

    @Bean("txManager2")
    public PlatformTransactionManager txManager2(){
        return new PlatformTransactionManager() {
            @Override
            public TransactionStatus getTransaction(TransactionDefinition transactionDefinition) throws TransactionException {
                return new SimpleTransactionStatus();
            }

            @Override
            public void commit(TransactionStatus transactionStatus) throws TransactionException {
                log.info("txManager2 : execute commit ...");
            }

            @Override
            public void rollback(TransactionStatus transactionStatus) throws TransactionException {
                log.info("txManager2 : execute rollback ...");
            }
        };
    }

    private static void testEnableHelloWorldConfiguration(ConfigurableApplicationContext context){
        Object helloWorld = context.getBean("helloWorld");
        log.info("helloWorld bean is :[{}]", helloWorld);
    }

    private static void testEnableImportSelectorConfiguration(ConfigurableApplicationContext context){
        ImportSelectorBean bean = context.getBean(ImportSelectorBean.class);
        log.info("importSelectorBean is : [{}]", bean);
        log.info("execute method helloWord ...");
        bean.helloWorld();
    }

    private static void testEnableImportBeanDefinitionRegistrarConfiguration(ConfigurableApplicationContext context){
        ImportBeanDefinitionRegistrarConfigurationBean bean = context.getBean(ImportBeanDefinitionRegistrarConfigurationBean.class);
        log.info("importBeanDefinitionRegistrarConfigurationBean is : [{}]", bean);
        log.info("execute method helloWord ...");
        bean.helloWorld();
    }

    @ConditionOnTest(name = "condition", value = "one")
    @Bean("conditionService")
    public String condition1 () {
        log.info("condition1 executed ...");
        return "one";
    }

    @ConditionOnTest(name = "condition", value = "two")
    @Bean("conditionService")
    public String condition2 () {
        log.info("condition2 executed ...");
        return "two";
    }


    public static void testFactoryBean(ConfigurableApplicationContext context) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MyFactoryBean.class)
                .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)
                .addPropertyValue("msg", "111");

        BeanDefinitionHolder holder = new BeanDefinitionHolder(builder.getBeanDefinition(), "xin.zero2one.bean.FactoryBeanTest");

        BeanDefinitionReaderUtils.registerBeanDefinition(holder, (DefaultListableBeanFactory)context.getBeanFactory());

        FactoryBeanTest factoryBeanTest = (FactoryBeanTest) context.getBeanFactory().getBean("xin.zero2one.bean.FactoryBeanTest");
        System.out.println(factoryBeanTest.sayHello());
    }

}

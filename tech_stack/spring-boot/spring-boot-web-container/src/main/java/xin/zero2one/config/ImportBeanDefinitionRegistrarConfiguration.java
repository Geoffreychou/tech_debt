package xin.zero2one.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author ZJD
 * @date 2019/6/3
 */
public class ImportBeanDefinitionRegistrarConfiguration implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        BeanDefinitionReaderUtils.registerWithGeneratedName(
                BeanDefinitionBuilder.genericBeanDefinition("xin.zero2one.bean.ImportBeanDefinitionRegistrarConfigurationBean")
                        .getBeanDefinition(),
                registry);
    }
}

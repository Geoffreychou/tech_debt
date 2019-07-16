package xin.zero2one.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author ZJD
 * @date 2019/6/3
 */
public class ImportSelectorConfiguration implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{"xin.zero2one.bean.ImportSelectorBean"};
    }
}

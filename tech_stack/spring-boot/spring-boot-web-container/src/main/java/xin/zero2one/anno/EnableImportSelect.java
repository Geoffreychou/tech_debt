package xin.zero2one.anno;

import org.springframework.context.annotation.Import;
import xin.zero2one.bean.ImportSelectorBean;

import java.lang.annotation.*;

/**
 * @author ZJD
 * @date 2019/6/3
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ImportSelectorBean.class)
public @interface EnableImportSelect {
}

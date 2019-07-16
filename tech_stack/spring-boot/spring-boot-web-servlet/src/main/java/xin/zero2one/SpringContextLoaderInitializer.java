package xin.zero2one;

import org.springframework.web.context.AbstractContextLoaderInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * @author zhoujundong
 * @data 6/18/2019
 * @description TODO
 */
public class SpringContextLoaderInitializer extends AbstractContextLoaderInitializer {
    @Override
    protected WebApplicationContext createRootApplicationContext() {
        return new AnnotationConfigWebApplicationContext();
    }
}

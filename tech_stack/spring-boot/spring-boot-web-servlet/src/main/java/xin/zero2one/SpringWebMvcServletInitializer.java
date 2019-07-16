package xin.zero2one;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


/**
 * @author zhoujundong
 * @data 6/17/2019
 * @description TODO
 */
public class SpringWebMvcServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return of(SpringWebMvcConfiguration.class);
    }

    @Override
    protected String[] getServletMappings() {
        return of("/*");
    }

    private static <T> T[] of (T... values) {
        return values;
    }
}

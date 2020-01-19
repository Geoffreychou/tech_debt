package xin.zero2one.factory;

import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import xin.zero2one.bean.FactoryBeanTest;

/**
 * @author zhoujundong
 * @data 1/19/2020
 * @description TODO
 */
public class MyFactoryBean implements FactoryBean<FactoryBeanTest> {

    @Setter
    private String msg;

    @Override
    public FactoryBeanTest getObject() throws Exception {
        return new FactoryBeanTest(msg);
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}

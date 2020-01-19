package xin.zero2one.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhoujundong
 * @data 1/19/2020
 * @description
 */
public class FactoryBeanTest {

    private String msg;

    public FactoryBeanTest(String msg) {
        this.msg = msg;
    }

    public String sayHello() {
        return this.msg;
    }

}

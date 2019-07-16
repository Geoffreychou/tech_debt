package xin.zero2one.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * @author zhoujundong
 * @data 6/25/2019
 * @description TODO
 */
public class ConditionTest implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionOnTest.class.getName());
        String value = (String) attributes.get("value");
        String name = (String) attributes.get("name");
        return value.equals(System.getProperty(name));
    }
}

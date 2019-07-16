package xin.zero2one.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ZJD
 * @date 2019/4/21
 */
@Configuration
@Import(WebConfig.class)
public class WebAutoConfig {
}

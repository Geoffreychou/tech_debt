package xin.zero2one.config;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

/**
 * @author ZJD
 * @date 2020/2/24
 */
@Getter
@Setter
@Slf4j
@Configuration
@PropertySource(value = {"classpath:application-test1.properties", "classpath:application-test2.properties"})
public class ApplicationConfiguration {

    @Value("${test.profile}")
    private String profile;

    @PostConstruct
    private void init() {
        log.info("profile: {}", profile);
    }



}

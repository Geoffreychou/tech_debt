package xin.zero2one.profile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author zhoujundong
 * @data 6/24/2019
 * @description TODO
 */
@Slf4j
@Profile("profile1")
@Component("profileService")
public class ProfileServiceOne implements IProfileService {

    @Override
    public void sayHi(String msg) {
        log.info("profile1 : {}", msg);
    }

}

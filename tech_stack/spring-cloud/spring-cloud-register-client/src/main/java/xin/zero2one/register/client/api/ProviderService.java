package xin.zero2one.register.client.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author zhoujundong
 * @data 1/17/2020
 * @description
 */
@FeignClient("register-client2")
@RequestMapping("/api/provider")
public interface ProviderService {

    @RequestMapping(method = RequestMethod.GET, value = "/{msg}")
    String hello(@PathVariable("msg") String msg);


}

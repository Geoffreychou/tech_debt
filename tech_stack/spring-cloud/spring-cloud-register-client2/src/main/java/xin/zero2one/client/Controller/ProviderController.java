package xin.zero2one.client.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoujundong
 * @data 1/17/2020
 * @description
 */
@RestController
@RequestMapping("/api/provider")
public class ProviderController {

    @GetMapping("/{msg}")
    public String hello(@PathVariable("msg") String msg) {
        return msg;
    }
}

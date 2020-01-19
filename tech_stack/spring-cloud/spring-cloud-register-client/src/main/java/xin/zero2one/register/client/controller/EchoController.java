package xin.zero2one.register.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xin.zero2one.register.client.api.ProviderService;

/**
 * @author zhoujundong
 * @data 1/16/2020
 * @description
 */
@RestController
@RequestMapping("/api/echo")
public class EchoController {

    @Autowired
    private ProviderService providerService;

    @GetMapping("/hello")
    public String hello(@RequestParam("msg") String msg) {
        return msg;
    }

    @GetMapping("/provider")
    public String helloProvider(@RequestParam("msg") String msg) {
        return providerService.hello(msg);
    }

}

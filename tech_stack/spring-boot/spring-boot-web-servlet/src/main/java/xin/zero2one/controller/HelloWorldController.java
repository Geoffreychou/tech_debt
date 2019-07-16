package xin.zero2one.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhoujundong
 * @data 6/17/2019
 * @description TODO
 */
@Controller
public class HelloWorldController {

    @RequestMapping
    @ResponseBody
    public String helloWorld() {
        return "hello, world!";
    }

}

package com.kevin.grab.controller;

import com.kevin.grab.utils.CsdnUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping(value = "/sayHello", method = RequestMethod.GET)
    public String sayHello(){
        return "Hello Kevin";
    }

    @RequestMapping(value = "/loginCsdn", method = RequestMethod.GET)
    public String loginCsdn(){
        return CsdnUtils.refreshCookie();
    }
}

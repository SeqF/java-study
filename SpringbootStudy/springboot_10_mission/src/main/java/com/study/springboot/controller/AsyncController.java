package com.study.springboot.controller;

import com.study.springboot.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AsyncController {

    @Autowired
    AsyncService asyncService;


    @RequestMapping("/hello")
    public String hello(){

        //停止三秒
        asyncService.hello();
        return "OK";
    }
}

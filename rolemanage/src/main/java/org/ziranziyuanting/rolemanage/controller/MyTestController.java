package org.ziranziyuanting.rolemanage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("mytest")
public class MyTestController {
    @GetMapping("test")
    public String getMethodName() {
        return LocalDateTime.now().toString();
    }
    
}

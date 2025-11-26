package org.ziranziyuanting.certificationcatalog.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class MyTestController {
    
    @GetMapping("test")
    public String test(){
        return "test/test : " + LocalDateTime.now();
    }
}

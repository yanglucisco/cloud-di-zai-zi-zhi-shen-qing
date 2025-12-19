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
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "test/test 现在时间 : " + LocalDateTime.now();
    }
}

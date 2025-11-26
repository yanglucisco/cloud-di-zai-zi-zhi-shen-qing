package org.ziranziyuanting.authcenter.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class MyTestController {
    @RequestMapping("test")
    public String test(){
        return "test/test 现在时间：" + LocalDateTime.now();
    }
}

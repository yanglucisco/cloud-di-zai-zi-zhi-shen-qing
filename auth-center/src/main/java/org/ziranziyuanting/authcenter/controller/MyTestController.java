package org.ziranziyuanting.authcenter.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.authcenter.entity.YlTest;
import org.ziranziyuanting.authcenter.service.YlTestService;

@RestController
@RequestMapping("test")
public class MyTestController {
    private final YlTestService ylTestService;
    public MyTestController(YlTestService ylTestService){
        this.ylTestService = ylTestService;
    }
    @RequestMapping("test")
    public String test(){
        return "test/test 现在时间：" + LocalDateTime.now();
    }
    @RequestMapping("yltest")
    public List<YlTest> ylTest(){
        return ylTestService.list();
    }
}

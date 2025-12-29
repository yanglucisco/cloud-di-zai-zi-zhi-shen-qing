package org.ziranziyuanting.authcenter.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.authcenter.entity.YlTest;
import org.ziranziyuanting.authcenter.service.YlTestService;

@RestController
@RequestMapping("test")
public class MyTestController {
    private final YlTestService ylTestService;
    private static final Logger logger = LoggerFactory.getLogger(MyTestController.class);
    public MyTestController(YlTestService ylTestService){
        this.ylTestService = ylTestService;
    }
    @RequestMapping("test")
    public String test(){
        return "test/test 现在时间：" + LocalDateTime.now();
    }
    @PostMapping("test")
    public String testPost(){
        return "test/testPost 现在时间：" + LocalDateTime.now();
    }
    @RequestMapping("yltest")
    public List<YlTest> ylTest(){
        // MDC.put("userId", "12345");
        // MDC.put("userName", "张三");
        // MDC.put("requestId", UUID.randomUUID().toString());
        logger.info("testMDC testMDC testMDC testMDC testMDC");
        // MDC.clear();
        return ylTestService.list();
    }
}

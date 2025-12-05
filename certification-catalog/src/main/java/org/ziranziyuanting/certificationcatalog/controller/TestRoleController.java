package org.ziranziyuanting.certificationcatalog.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("role")
public class TestRoleController {
    @GetMapping("test")
    public String test(){
        return "role/test " + LocalDateTime.now();
    }
}

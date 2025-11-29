package org.ziranziyuanting.certificationcatalog.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.certificationcatalog.model.MyUser;

@RestController
@RequestMapping("user")
public class MyUserController {
    @RequestMapping("user")
    public MyUser getMyUser(){
        return new MyUser("123yanglu " + LocalDateTime.now() , 40);
    }
}

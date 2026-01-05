package org.ziranziyuanting.certificationcatalog.controller;

import java.time.LocalDateTime;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.certificationcatalog.model.MyUser;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
public class MyUserController {
    @RequestMapping("user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN123123123')")
    public MyUser getMyUser(Authentication authentication, HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        return new MyUser("123yanglu " + LocalDateTime.now() , 40);
    }
}

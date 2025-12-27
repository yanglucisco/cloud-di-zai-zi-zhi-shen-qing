package org.ziranziyuanting.authcenter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.authcenter.param.UserRegistParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
// @RequestMapping("mytest")
public class UserController {
    private final PasswordEncoder passwordEncoder;

    public UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("regist")
    public ResponseEntity<String> regist() {
        // 开始时间
        long startTime = System.currentTimeMillis();
        String pass = passwordEncoder.encode("adminpwd");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("执行耗时: " + duration + " 毫秒");
        System.out.println("执行耗时: " + (duration / 1000.0) + " 秒");
        // UserDetails user = User.withDefaultPasswordEncoder()
        //         .username("user")
        //         .password("password")
        //         .roles("user")
        //         .build();
        // System.out.println(user.getPassword());
        // {bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
        return ResponseEntity.ok("post注册成功: " + pass);
    }

    @GetMapping("regist")
    public ResponseEntity<String> getregist() {
        return ResponseEntity.ok("注册成功");
    }
}

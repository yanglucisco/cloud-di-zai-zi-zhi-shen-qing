package org.ziranziyuanting.authcenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.ziranziyuanting.authcenter.mapper")
public class AuthCenterApp {
    public static void main(String[] args) {
        System.out.println("Hello AuthCenterApp!");
        SpringApplication.run(AuthCenterApp.class, args);
    }
}
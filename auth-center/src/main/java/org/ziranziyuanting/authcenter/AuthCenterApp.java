package org.ziranziyuanting.authcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthCenterApp {
    public static void main(String[] args) {
        System.out.println("Hello AuthCenterApp!");
        SpringApplication.run(AuthCenterApp.class, args);
    }
}
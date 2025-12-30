package org.ziranziyuanting.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.ziranziyuanting.account","org.ziranziyuanting.common"})
public class AccountApp {
    public static void main(String[] args) {
        System.out.println("Hello AccountApp!");
        SpringApplication.run(AccountApp.class, args);
    }
}
package org.ziranziyuanting.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"org.ziranziyuanting.account", "org.ziranziyuanting.common.commonminio"})
public class AccountApp {
    public static void main(String[] args) {
        System.out.println("Hello AccountApp!");
        // test1();
        SpringApplication.run(AccountApp.class, args);
    }
}
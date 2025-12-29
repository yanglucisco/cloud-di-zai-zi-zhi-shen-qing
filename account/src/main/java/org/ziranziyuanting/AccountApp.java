package org.ziranziyuanting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountApp {
    public static void main(String[] args) {
        System.out.println("Hello AccountApp!");
        SpringApplication.run(AccountApp.class, args);
    }
}
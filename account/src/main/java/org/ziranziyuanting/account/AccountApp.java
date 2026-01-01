package org.ziranziyuanting.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class AccountApp {
    public static void main(String[] args) {
        System.out.println("Hello AccountApp!");
        // test1();
        SpringApplication.run(AccountApp.class, args);
    }
    private static void test1(){
        Mono.just("a").flatMap(s -> {
            return Mono.just(s + "b");
        }).subscribe(s -> {
            System.out.println(s);
        });
    }
}
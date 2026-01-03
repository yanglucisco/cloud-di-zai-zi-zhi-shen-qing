package org.ziranziyuanting.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @EnableDiscoveryClient
public class GatewayApp {
    public static void main(String[] args) {
        System.out.println("Hello GatewayApp!");
        SpringApplication.run(GatewayApp.class, args);
    }
}




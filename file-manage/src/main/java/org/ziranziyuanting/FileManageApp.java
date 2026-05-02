package org.ziranziyuanting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class FileManageApp {
    public static void main(String[] args) {
        System.out.println("Hello FileManageApp!");
        SpringApplication.run(FileManageApp.class, args);
    }
}
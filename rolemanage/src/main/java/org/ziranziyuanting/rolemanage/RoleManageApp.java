package org.ziranziyuanting.rolemanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RoleManageApp {
    public static void main(String[] args) {
        System.out.println("Hello RoleManageApp!");
        SpringApplication.run(RoleManageApp.class, args);
        // String s = new RoleManageApp().test();
        // System.out.println(s);
    }
    public String test(){
        try{
            String a = "abc";
            return a;
        }finally{
            String b = "bc";
            if(b.equals("bc")){
                return "bc";
            }
        }
    }
}
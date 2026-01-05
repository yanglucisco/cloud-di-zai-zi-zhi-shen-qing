package org.ziranziyuanting.certificationcatalog;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

@SpringBootApplication
public class CertificationCatalogApp {
    public static void main(String[] args) throws NacosException {
        System.out.println("Hello CertificationCatalogApp!");
        SpringApplication.run(CertificationCatalogApp.class, args);
        // final String remoteAddress = "localhost:8848";
        // final String groupId = "Sentinel_Demo";
        // final String dataId = "com.alibaba.csp.sentinel.demo.flow.rule";
        // final String rule = "[\n"
        //     + "  {\n"
        //     + "    \"resource\": \"TestResource\",\n"
        //     + "    \"controlBehavior\": 0,\n"
        //     + "    \"count\": 5.0,\n"
        //     + "    \"grade\": 1,\n"
        //     + "    \"limitApp\": \"default\",\n"
        //     + "    \"strategy\": 0\n"
        //     + "  }\n"
        //     + "]";
        // Properties properties = new Properties();
        //     properties.put("serverAddr", "127.0.0.1:8848"); // Nacos服务器地址
        //     // properties.put("namespace", "your_namespace_id"); // (可选) 命名空间
        //     properties.put("username", "nacos"); // 用户名
        //     properties.put("password", "Htht123.com"); // 密码 
        // ConfigService configService = NacosFactory.createConfigService(properties);
        // System.out.println(configService.publishConfig(dataId, groupId, rule));
    }
}
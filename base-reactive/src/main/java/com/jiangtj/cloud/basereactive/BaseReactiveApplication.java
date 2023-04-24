package com.jiangtj.cloud.basereactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BaseReactiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseReactiveApplication.class, args);
    }

}
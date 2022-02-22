package com.boojux.yygh.cmn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@ComponentScan(basePackages = {"com.boojux"})
@EnableDiscoveryClient
public class ServiceCmn {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmn.class,args);
    }
}

package com.boojux.serviceorder.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.boojux.serviceorder.mapper")
public class OrderConfig {
}

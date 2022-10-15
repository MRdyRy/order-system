package com.rudy.ryanto.order.system.order.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.rudy.ryanto.system.order.service.dataaccess")
@EntityScan(basePackages = "com.rudy.ryanto.order.system.order.service.dataaccess")
@SpringBootApplication(scanBasePackages = "com.rudy.ryanto.order.system")
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class,args);
    }
}

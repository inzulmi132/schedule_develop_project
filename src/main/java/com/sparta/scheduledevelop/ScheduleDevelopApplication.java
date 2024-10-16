package com.sparta.scheduledevelop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
public class ScheduleDevelopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleDevelopApplication.class, args);
    }

}

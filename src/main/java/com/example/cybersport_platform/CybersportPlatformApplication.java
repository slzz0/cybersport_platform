package com.example.cybersport_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CybersportPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(CybersportPlatformApplication.class, args);
    }
}

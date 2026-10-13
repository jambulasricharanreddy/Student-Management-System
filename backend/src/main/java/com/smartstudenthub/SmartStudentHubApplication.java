package com.smartstudenthub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SmartStudentHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartStudentHubApplication.class, args);
    }
}

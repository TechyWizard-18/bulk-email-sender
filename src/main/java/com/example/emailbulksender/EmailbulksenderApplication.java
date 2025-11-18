package com.example.emailbulksender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EmailbulksenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailbulksenderApplication.class, args);
    }

}

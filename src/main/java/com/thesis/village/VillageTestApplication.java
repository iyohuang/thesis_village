package com.thesis.village;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableAspectJAutoProxy
public class VillageTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(VillageTestApplication.class, args);
    }
}

package com.winterfoodies.winterfoodies_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // timestamp 추가하려고 넣음
@SpringBootApplication
public class WinterfoodiesProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(WinterfoodiesProjectApplication.class, args);
    }

}

package com.winterfoodies.winterfoodies_project;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaAuditing // timestamp 추가하려고 넣음
@SpringBootApplication
@EnableTransactionManagement //자동으로 트랜잭션 관리를 활성화하고, 트랜잭션 관리자를 구성. 일일이 메서드에 @Transaction을 붙이지 않아도됨!!!
@Configuration
public class WinterfoodiesProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(WinterfoodiesProjectApplication.class, args);

    }


//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

}

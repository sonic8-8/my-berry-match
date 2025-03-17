package com.gongcha.berrymatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling  // 스케줄링 활성화
public class BerryMatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BerryMatchApplication.class, args);
    }

}

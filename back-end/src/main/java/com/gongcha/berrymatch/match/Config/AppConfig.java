package com.gongcha.berrymatch.match.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Configuration
public class AppConfig {
    // 매칭대기열 테이블에 동시성문제로 잠구기위한 빈설정
    @Bean
    public Lock matchLock() {
        return new ReentrantLock();
    }
}

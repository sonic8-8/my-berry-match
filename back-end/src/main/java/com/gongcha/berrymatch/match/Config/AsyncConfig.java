package com.gongcha.berrymatch.match.Config;

import com.gongcha.berrymatch.match.domain.Match;
import com.gongcha.berrymatch.match.domain.MatchingQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "matchQueue")
    public BlockingQueue<Match> matchQueue() {
        return new LinkedBlockingQueue<>(1000); // 매칭 대기열 큐 (최대 1000개)
    }

    @Bean(name = "matchingProcessingQueue")
    public BlockingQueue<MatchingQueue> matchingProcessingQueue() {
        return new LinkedBlockingQueue<>(1000); // 매칭 처리 대기 큐
    }


    @Bean(name = "matchStatusTaskExecutor")
    public Executor matchStatusTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);   // 매치 상태 업데이트에 대한 기본 스레드 수
        executor.setMaxPoolSize(10);   // 매치 상태 업데이트에 대한 최대 스레드 수
        executor.setQueueCapacity(200); // 매치 상태 업데이트 큐의 최대 크기
        executor.setThreadNamePrefix("MatchStatusAsync-"); // 스레드 이름 접두사
        executor.initialize();
        return executor;
    }


    @Bean(name = "queueStatusTaskExecutor")
    public Executor queueStatusTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);   // 기본 스레드 수
        executor.setMaxPoolSize(10);   // 최대 스레드 수
        executor.setQueueCapacity(200); // 큐의 최대 크기
        executor.setThreadNamePrefix("QueueStatusAsync-"); // 스레드 이름 접두사
        executor.initialize();
        return executor;
    }


    @Bean(name = "individual matchingTaskExecutor")
    public Executor individualmatchingTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Matching-Thread-");
        executor.initialize();
        return executor;
    }


    @Bean(name = "group matchingTaskExecutor")
    public Executor groupmatchingTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Matching-Thread-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "MatchEenTaskExecutor")
    public Executor MatchEenTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Matching-Thread-");
        executor.initialize();
        return executor;
    }
}

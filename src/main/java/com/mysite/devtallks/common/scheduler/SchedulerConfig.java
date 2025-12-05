package com.mysite.devtallks.common.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    /**
     * 스케줄러 전용 스레드풀.
     * - 필요에 따라 pool size를 application.properties로 외부화해도 좋다.
     */
    @Bean("schedulerTaskExecutor")
    public Executor taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(6);
        scheduler.setThreadNamePrefix("devtallks-sched-");
        scheduler.setAwaitTerminationSeconds(30);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }
}

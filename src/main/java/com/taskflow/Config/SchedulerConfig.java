package com.taskflow.Config;

import com.taskflow.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final NotificationService notificationService;

    @Scheduled(fixedRate = 900000)
    public void checkUpcomingDeadlines() {
        notificationService.checkUpcomingTasks();
    }
}

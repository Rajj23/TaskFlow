package com.taskflow.Service.Implemention;

import com.taskflow.DTO.Notification.NotificationMessage;
import com.taskflow.Entity.Task;
import com.taskflow.Repo.TaskRepo;
import com.taskflow.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final TaskRepo taskRepo;
    private final SimpMessagingTemplate messagingTemplate;

    private final Set<Long> notifiedTasks = ConcurrentHashMap.newKeySet();

    @Override
    public void checkUpcomingTasks() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.plusMinutes(60);

        log.info("Checking for tasks due between {} and {}", now, threshold);

        List<Task> upcomingTasks = taskRepo.findTasksWithDeadlineBetween(now, threshold);

        int notificationsSent = 0;
        for (Task task : upcomingTasks) {
            if (!notifiedTasks.contains(task.getId()) &&
                    task.getDeadline() != null &&
                    task.getUser() != null) {

                sendNotification(task);
                notifiedTasks.add(task.getId());
                notificationsSent++;
            }
        }

        log.info("Sent {} notifications for upcoming tasks", notificationsSent);

        cleanupOldNotifications();
    }

    private void sendNotification(Task task) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String message = String.format("âš ï¸ Task '%s' is due at %s",
                    task.getTitle(),
                    task.getDeadline().format(formatter));

            NotificationMessage notification = new NotificationMessage(
                    task.getUser().getUsername(),
                    message);

            messagingTemplate.convertAndSendToUser(
                    task.getUser().getUsername(),
                    "/queue/notifications",
                    notification);

            log.debug("Sent notification to user {} for task: {}",
                    task.getUser().getUsername(), task.getTitle());

        } catch (Exception e) {
            log.error("Failed to send notification for task {}: {}",
                    task.getId(), e.getMessage());
        }
    }

    private void cleanupOldNotifications() {
        LocalDateTime now = LocalDateTime.now();
        List<Task> pastTasks = taskRepo.findTasksWithDeadlineBefore(now);

        for (Task task : pastTasks) {
            notifiedTasks.remove(task.getId());
        }
    }

    @Override
    public void sendTaskCreatedNotification(String username, String taskTitle) {
        try {
            String message = String.format("âœ… New task created: '%s'", taskTitle);
            NotificationMessage notification = new NotificationMessage(username, message);

            messagingTemplate.convertAndSendToUser(
                    username,
                    "/queue/notifications",
                    notification);

            log.debug("Sent task creation notification to user {}: {}", username, taskTitle);
        } catch (Exception e) {
            log.error("Failed to send task creation notification: {}", e.getMessage());
        }
    }

    @Override
    public void sendTaskCompletedNotification(String username, String taskTitle) {
        try {
            String message = String.format("ðŸŽ‰ Task completed: '%s'", taskTitle);
            NotificationMessage notification = new NotificationMessage(username, message);

            messagingTemplate.convertAndSendToUser(
                    username,
                    "/queue/notifications",
                    notification);

            log.debug("Sent task completion notification to user {}: {}", username, taskTitle);
        } catch (Exception e) {
            log.error("Failed to send task completion notification: {}", e.getMessage());
        }
    }

    @Override
    public void sendTaskOverdueNotification(String username, String taskTitle) {
        try {
            String message = String.format("ðŸš¨ Task overdue: '%s'", taskTitle);
            NotificationMessage notification = new NotificationMessage(username, message);

            messagingTemplate.convertAndSendToUser(
                    username,
                    "/queue/notifications",
                    notification);

            log.debug("Sent overdue notification to user {}: {}", username, taskTitle);
        } catch (Exception e) {
            log.error("Failed to send overdue notification: {}", e.getMessage());
        }
    }
}

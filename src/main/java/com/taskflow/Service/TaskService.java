package com.taskflow.Service;

import com.taskflow.DTO.Task.TaskRequest;
import com.taskflow.DTO.Task.TaskResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest request, String username);

    Page<TaskResponse> getAllTask(String username, int page, int size);

    TaskResponse updateTask(Long taskId, TaskRequest request, String username);

    void deleteTask(Long taskId, String username);

    List<TaskResponse> sortByDeadline(String username, int page, int size, String order);

    List<TaskResponse> sortByPriority(String username, int page, int size, String order);

    List<TaskResponse> searchTasks(String query, String username);

    List<TaskResponse> filterByDateRange(LocalDateTime start, LocalDateTime end, String query, String username);

    List<TaskResponse> getAllTasksForAdmin();

    void deleteAnyTask(Long taskId);

    TaskResponse updateAnyTask(Long taskId, TaskRequest request);

    int countTasks(String username);
}

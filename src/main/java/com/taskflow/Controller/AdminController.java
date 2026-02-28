package com.taskflow.Controller;

import com.taskflow.DTO.Task.TaskRequest;
import com.taskflow.DTO.Task.TaskResponse;
import com.taskflow.DTO.User.UserResponse;
import com.taskflow.Service.TaskService;
import com.taskflow.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task/admin")
@RequiredArgsConstructor
public class AdminController {

    private final TaskService taskService;
    private final UserService userService;


    @GetMapping("/all-tasks")
    public ResponseEntity<List<TaskResponse>> getAllTaskFromAdmin(){
        List<TaskResponse> tasks = taskService.getAllTasksForAdmin();
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("delete/{taskId}")
    public ResponseEntity<String> deleteAnyTask(@PathVariable Long taskId){
        taskService.deleteAnyTask(taskId);
        return ResponseEntity.ok("Task deleted by admin");
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<TaskResponse> updateAnyTask(@PathVariable Long taskId, @RequestBody TaskRequest request){
        TaskResponse updated = taskService.updateAnyTask(taskId,request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
}

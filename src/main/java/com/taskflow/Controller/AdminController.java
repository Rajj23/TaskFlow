package com.taskflow.Controller;

import com.taskflow.DTO.Task.TaskRequest;
import com.taskflow.DTO.Task.TaskResponse;
import com.taskflow.DTO.User.UserResponse;
import com.taskflow.Service.TaskService;
import com.taskflow.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin", description = "Admin-only operations — ADMIN role + JWT required")
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/task/admin")
@RequiredArgsConstructor
public class AdminController {

    private final TaskService taskService;
    private final UserService userService;

    @Operation(summary = "Get all tasks (admin)", description = "Returns every task in the system.")
    @ApiResponse(responseCode = "200", description = "All tasks returned")
    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN role required", content = @Content)
    @GetMapping("/all-tasks")
    public ResponseEntity<List<TaskResponse>> getAllTaskFromAdmin() {
        List<TaskResponse> tasks = taskService.getAllTasksForAdmin();
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Delete any task (admin)", description = "Admin can delete any task regardless of owner.")
    @ApiResponse(responseCode = "200", description = "Task deleted")
    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN role required", content = @Content)
    @DeleteMapping("delete/{taskId}")
    public ResponseEntity<String> deleteAnyTask(@PathVariable Long taskId) {
        taskService.deleteAnyTask(taskId);
        return ResponseEntity.ok("Task deleted by admin");
    }

    @Operation(summary = "Update any task (admin)", description = "Admin can update any task regardless of owner.")
    @ApiResponse(responseCode = "200", description = "Task updated")
    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN role required", content = @Content)
    @PutMapping("/update/{taskId}")
    public ResponseEntity<TaskResponse> updateAnyTask(@PathVariable Long taskId, @RequestBody TaskRequest request) {
        TaskResponse updated = taskService.updateAnyTask(taskId, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Get all users (admin)", description = "Returns a list of all registered users.")
    @ApiResponse(responseCode = "200", description = "Users returned")
    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN role required", content = @Content)
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}

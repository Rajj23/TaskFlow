package com.taskflow.Controller;

import com.taskflow.DTO.Page.PageResponse;
import com.taskflow.DTO.Task.TaskRequest;
import com.taskflow.DTO.Task.TaskResponse;
import com.taskflow.Service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Tasks", description = "CRUD and filtering operations for tasks — JWT required")
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Create a task", description = "Creates a new task for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Task created", content = @Content(schema = @Schema(implementation = TaskResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content)
    @PostMapping("/create")
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(taskService.createTask(request, username));
    }

    @Operation(summary = "Get all tasks (paginated)", description = "Returns a page of tasks for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Page of tasks returned")
    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content)
    @GetMapping("/gettasks")
    public ResponseEntity<Page<TaskResponse>> getAllTask(
            Authentication authentication,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        String username = authentication.getName();
        return ResponseEntity.ok(taskService.getAllTask(username, page, size));
    }

    @Operation(summary = "Update a task", description = "Updates a task owned by the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Task updated")
    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden — not your task", content = @Content)
    @PutMapping("/update/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(taskService.updateTask(taskId, request, username));
    }

    @Operation(summary = "Delete a task", description = "Deletes a task owned by the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Task deleted")
    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden — not your task", content = @Content)
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> delete(@PathVariable Long taskId, Authentication authentication) {
        String username = authentication.getName();
        taskService.deleteTask(taskId, username);
        return ResponseEntity.ok("Successfully deleted");
    }

    @Operation(summary = "Sort by deadline", description = "Returns a paginated list of tasks sorted by deadline.")
    @ApiResponse(responseCode = "200", description = "Sorted tasks returned")
    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content)
    @GetMapping("/deadline")
    public ResponseEntity<PageResponse<TaskResponse>> sortByDeadLine(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort direction: asc or desc") @RequestParam(defaultValue = "asc") String order) {
        String username = authentication.getName();
        List<TaskResponse> pageContent = taskService.sortByDeadline(username, page, size, order);
        int totalTasks = taskService.countTasks(username);
        int totalPages = (int) Math.ceil((double) totalTasks / size);
        return ResponseEntity.ok(new PageResponse<>(pageContent, totalPages, page, totalTasks));
    }

    @Operation(summary = "Sort by priority", description = "Returns a paginated list of tasks sorted by priority.")
    @ApiResponse(responseCode = "200", description = "Sorted tasks returned")
    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content)
    @GetMapping("/priority")
    public ResponseEntity<PageResponse<TaskResponse>> sortByPriority(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort direction: asc or desc") @RequestParam(defaultValue = "asc") String order) {
        String username = authentication.getName();
        List<TaskResponse> pageContent = taskService.sortByPriority(username, page, size, order);
        int totalTasks = taskService.countTasks(username);
        int totalPages = (int) Math.ceil((double) totalTasks / size);
        return ResponseEntity.ok(new PageResponse<>(pageContent, totalPages, page, totalTasks));
    }

    @Operation(summary = "Search tasks", description = "Free-text search over the authenticated user's tasks.")
    @ApiResponse(responseCode = "200", description = "Matching tasks returned")
    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content)
    @GetMapping("/search")
    public ResponseEntity<List<TaskResponse>> searchTasks(
            @Parameter(description = "Search query string") @RequestParam String query,
            Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(taskService.searchTasks(query, username));
    }

    @Operation(summary = "Filter tasks", description = "Filter tasks by date range and/or status.")
    @ApiResponse(responseCode = "200", description = "Filtered tasks returned")
    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content)
    @GetMapping("/filter")
    public ResponseEntity<List<TaskResponse>> filterTask(
            @Parameter(description = "Range start (ISO date-time)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "Range end (ISO date-time)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @Parameter(description = "Task status filter") @RequestParam(required = false) String status,
            Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(taskService.filterByDateRange(start, end, status, username));
    }
}

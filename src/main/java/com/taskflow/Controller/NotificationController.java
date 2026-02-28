package com.taskflow.Controller;

import com.taskflow.Service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Notifications", description = "Deadline notification triggers — JWT required")
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Trigger upcoming-task check", description = "Manually triggers the deadline notification check for all users.")
    @ApiResponse(responseCode = "200", description = "Notification check completed")
    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content)
    @PostMapping("/check")
    public ResponseEntity<String> checkUpcomingTasks() {
        notificationService.checkUpcomingTasks();
        return ResponseEntity.ok("Notification check completed");
    }
}

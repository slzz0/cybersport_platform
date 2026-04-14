package com.example.cybersport_platform.dto.response;

import com.example.cybersport_platform.service.AsyncTaskExecutionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "Async task status response")
public class AsyncTaskStatusResponse {

    @Schema(description = "Task identifier", example = "1")
    private Long taskId;

    @Schema(description = "Business operation name", example = "Generate tournament report")
    private String operationName;

    @Schema(description = "Current task status", example = "RUNNING")
    private AsyncTaskExecutionStatus status;

    @Schema(description = "Requested duration in milliseconds", example = "3000")
    private long durationMs;

    @Schema(description = "Completed progress steps", example = "3")
    private int completedSteps;

    @Schema(description = "Current progress percent", example = "37")
    private int progressPercent;

    @Schema(description = "Estimated remaining time in milliseconds", example = "5000")
    private long remainingMs;

    @Schema(description = "Task creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Task start timestamp")
    private LocalDateTime startedAt;

    @Schema(description = "Task completion timestamp")
    private LocalDateTime completedAt;

    @Schema(description = "Task result text", example = "Operation completed successfully")
    private String result;

    @Schema(description = "Failure message if task ended with error")
    private String errorMessage;
}

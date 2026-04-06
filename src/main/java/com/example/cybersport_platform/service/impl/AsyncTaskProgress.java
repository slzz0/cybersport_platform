package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.dto.response.AsyncTaskStatusResponse;
import com.example.cybersport_platform.service.AsyncTaskExecutionStatus;

import java.time.LocalDateTime;

public class AsyncTaskProgress {

    private final Long taskId;
    private final String operationName;
    private final long durationMs;
    private final LocalDateTime createdAt;
    private AsyncTaskExecutionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String result;
    private String errorMessage;

    public AsyncTaskProgress(Long taskId, String operationName, long durationMs) {
        this.taskId = taskId;
        this.operationName = operationName;
        this.durationMs = durationMs;
        this.createdAt = LocalDateTime.now();
        this.status = AsyncTaskExecutionStatus.PENDING;
    }

    public synchronized AsyncTaskExecutionStatus getStatus() {
        return status;
    }

    public synchronized void markRunning() {
        status = AsyncTaskExecutionStatus.RUNNING;
        startedAt = LocalDateTime.now();
    }

    public synchronized void markCompleted(String taskResult) {
        status = AsyncTaskExecutionStatus.COMPLETED;
        completedAt = LocalDateTime.now();
        result = taskResult;
        errorMessage = null;
    }

    public synchronized void markFailed(String taskErrorMessage) {
        status = AsyncTaskExecutionStatus.FAILED;
        completedAt = LocalDateTime.now();
        errorMessage = taskErrorMessage;
        result = null;
    }

    public synchronized AsyncTaskStatusResponse toResponse() {
        return new AsyncTaskStatusResponse(
                taskId,
                operationName,
                status,
                durationMs,
                createdAt,
                startedAt,
                completedAt,
                result,
                errorMessage
        );
    }
}

package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.dto.response.AsyncTaskStatusResponse;
import com.example.cybersport_platform.service.AsyncTaskExecutionStatus;

import java.time.LocalDateTime;

public class AsyncTaskProgress {

    private final Long taskId;
    private final String operationName;
    private final long durationMs;
    private final int totalSteps;
    private final LocalDateTime createdAt;
    private AsyncTaskExecutionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String result;
    private String errorMessage;
    private int completedSteps;

    public AsyncTaskProgress(Long taskId, String operationName, long durationMs) {
        this.taskId = taskId;
        this.operationName = operationName;
        this.durationMs = durationMs;
        this.totalSteps = Math.max(1, (int) Math.ceil(durationMs / 1000.0));
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

    public synchronized void markStepCompleted(int stepsCompleted) {
        completedSteps = Math.min(stepsCompleted, totalSteps);
    }

    public synchronized void markCompleted(String taskResult) {
        status = AsyncTaskExecutionStatus.COMPLETED;
        completedAt = LocalDateTime.now();
        completedSteps = totalSteps;
        result = taskResult;
        errorMessage = null;
    }

    public synchronized void markFailed(String taskErrorMessage) {
        status = AsyncTaskExecutionStatus.FAILED;
        completedAt = LocalDateTime.now();
        errorMessage = taskErrorMessage;
        result = null;
    }

    public synchronized String getOperationName() {
        return operationName;
    }

    public synchronized long getDurationMs() {
        return durationMs;
    }

    public synchronized int getTotalSteps() {
        return totalSteps;
    }

    public synchronized AsyncTaskStatusResponse toResponse() {
        int progressPercent = totalSteps == 0 ? 0 : (completedSteps * 100) / totalSteps;
        if (status == AsyncTaskExecutionStatus.COMPLETED) {
            progressPercent = 100;
        }
        long remainingMs = calculateRemainingMs();
        return new AsyncTaskStatusResponse(
                taskId,
                operationName,
                status,
                durationMs,
                completedSteps,
                progressPercent,
                remainingMs,
                createdAt,
                startedAt,
                completedAt,
                result,
                errorMessage
        );
    }

    private long calculateRemainingMs() {
        if (status == AsyncTaskExecutionStatus.COMPLETED || status == AsyncTaskExecutionStatus.FAILED) {
            return 0L;
        }
        if (startedAt == null) {
            return durationMs;
        }
        long elapsedMs = java.time.Duration.between(startedAt, LocalDateTime.now()).toMillis();
        return Math.max(durationMs - elapsedMs, 0L);
    }
}

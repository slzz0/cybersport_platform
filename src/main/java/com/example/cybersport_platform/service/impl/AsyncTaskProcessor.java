package com.example.cybersport_platform.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncTaskProcessor {

    @Async("businessTaskExecutor")
    public CompletableFuture<Void> processTask(Long taskId, ConcurrentMap<Long, AsyncTaskProgress> tasks) {
        AsyncTaskProgress task = tasks.get(taskId);
        if (task == null) {
            return CompletableFuture.completedFuture(null);
        }

        try {
            task.markRunning();
            log.info(
                    "Async task started in background: taskId={}, operationName='{}', durationMs={}",
                    taskId,
                    task.toResponse().getOperationName(),
                    task.toResponse().getDurationMs()
            );
            Thread.sleep(task.toResponse().getDurationMs());
            String result = "Business operation '" + task.toResponse().getOperationName()
                    + "' completed successfully";
            task.markCompleted(result);
            log.info(
                    "Async task completed: taskId={}, finalStatus={}, result='{}'",
                    taskId,
                    task.toResponse().getStatus(),
                    task.toResponse().getResult()
            );
            return CompletableFuture.completedFuture(null);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            task.markFailed("Task execution was interrupted");
            log.warn("Async task interrupted: taskId={}, message={}", taskId, exception.getMessage());
            return CompletableFuture.failedFuture(exception);
        } catch (Exception exception) {
            task.markFailed(exception.getMessage());
            log.error("Async task failed: taskId={}, message={}", taskId, exception.getMessage(), exception);
            return CompletableFuture.failedFuture(exception);
        }
    }
}

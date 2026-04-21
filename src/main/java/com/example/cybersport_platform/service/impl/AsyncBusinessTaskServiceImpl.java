package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.dto.request.AsyncBusinessTaskRequest;
import com.example.cybersport_platform.dto.response.AsyncTaskStartResponse;
import com.example.cybersport_platform.dto.response.AsyncTaskStatusResponse;
import com.example.cybersport_platform.dto.response.AsyncTasksOverviewResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.service.AsyncTaskExecutionStatus;
import com.example.cybersport_platform.service.AsyncBusinessTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncBusinessTaskServiceImpl implements AsyncBusinessTaskService {

    private static final String TASK_NOT_FOUND_MESSAGE = "Task not found: ";

    private final AtomicLong taskIdGenerator = new AtomicLong();
    private final ConcurrentMap<Long, AsyncTaskProgress> tasks = new ConcurrentHashMap<>();
    private final AsyncTaskProcessor asyncTaskProcessor;

    @Override
    public AsyncTaskStartResponse startTask(AsyncBusinessTaskRequest request) {
        long taskId = taskIdGenerator.incrementAndGet();
        AsyncTaskProgress task = new AsyncTaskProgress(taskId, request.getOperationName(), request.getDurationMs());
        tasks.put(taskId, task);
        log.info(
                "Async task accepted: taskId={}, operationName='{}', durationMs={}, initialStatus={}",
                taskId,
                request.getOperationName(),
                request.getDurationMs(),
                task.getStatus()
        );
        asyncTaskProcessor.processTask(taskId, tasks);
        return new AsyncTaskStartResponse(taskId, task.getStatus(), task.getDurationMs());
    }

    @Override
    public AsyncTaskStatusResponse getTaskStatus(Long taskId) {
        AsyncTaskProgress task = tasks.get(taskId);
        if (task == null) {
            throw new NotFoundException(TASK_NOT_FOUND_MESSAGE + taskId);
        }
        AsyncTaskStatusResponse response = task.toResponse();
        log.info(
                "Async task status requested: taskId={}, status={}, completedSteps={}, "
                        + "progressPercent={}, result='{}', errorMessage='{}'",
                response.getTaskId(),
                response.getStatus(),
                response.getCompletedSteps(),
                response.getProgressPercent(),
                response.getResult(),
                response.getErrorMessage()
        );
        return response;
    }

    @Override
    public AsyncTasksOverviewResponse getAllTaskStatuses() {
        List<AsyncTaskStatusResponse> taskResponses = tasks.values().stream()
                .map(AsyncTaskProgress::toResponse)
                .sorted(Comparator.comparing(AsyncTaskStatusResponse::getTaskId))
                .toList();

        int pendingTasks = countByStatus(taskResponses, AsyncTaskExecutionStatus.PENDING);
        int runningTasks = countByStatus(taskResponses, AsyncTaskExecutionStatus.RUNNING);
        int completedTasks = countByStatus(taskResponses, AsyncTaskExecutionStatus.COMPLETED);
        int failedTasks = countByStatus(taskResponses, AsyncTaskExecutionStatus.FAILED);

        AsyncTasksOverviewResponse response = new AsyncTasksOverviewResponse(
                taskResponses.size(),
                pendingTasks,
                runningTasks,
                completedTasks,
                failedTasks,
                taskResponses
        );
        log.info(
                "Async task overview requested: totalTasks={}, pendingTasks={}, runningTasks={}, "
                        + "completedTasks={}, failedTasks={}",
                response.getTotalTasks(),
                response.getPendingTasks(),
                response.getRunningTasks(),
                response.getCompletedTasks(),
                response.getFailedTasks()
        );
        return response;
    }

    private int countByStatus(List<AsyncTaskStatusResponse> taskResponses, AsyncTaskExecutionStatus status) {
        return (int) taskResponses.stream()
            .filter(task -> task.getStatus() == status)
            .count();
    }
}

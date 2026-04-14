package com.example.cybersport_platform.service;

import com.example.cybersport_platform.dto.request.AsyncBusinessTaskRequest;
import com.example.cybersport_platform.dto.response.AsyncTaskStartResponse;
import com.example.cybersport_platform.dto.response.AsyncTaskStatusResponse;
import com.example.cybersport_platform.dto.response.AsyncTasksOverviewResponse;

public interface AsyncBusinessTaskService {

    AsyncTaskStartResponse startTask(AsyncBusinessTaskRequest request);

    AsyncTaskStatusResponse getTaskStatus(Long taskId);

    AsyncTasksOverviewResponse getAllTaskStatuses();
}

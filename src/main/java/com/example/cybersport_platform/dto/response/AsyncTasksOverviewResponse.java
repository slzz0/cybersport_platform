package com.example.cybersport_platform.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "Overview of all asynchronous tasks")
public class AsyncTasksOverviewResponse {

    @Schema(description = "Total number of tasks", example = "5")
    private int totalTasks;

    @Schema(description = "Number of pending tasks", example = "1")
    private int pendingTasks;

    @Schema(description = "Number of running tasks", example = "2")
    private int runningTasks;

    @Schema(description = "Number of completed tasks", example = "2")
    private int completedTasks;

    @Schema(description = "Number of failed tasks", example = "0")
    private int failedTasks;

    @Schema(description = "Detailed task statuses")
    private List<AsyncTaskStatusResponse> tasks;
}

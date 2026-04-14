package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.dto.request.AsyncBusinessTaskRequest;
import com.example.cybersport_platform.dto.response.AsyncTaskStartResponse;
import com.example.cybersport_platform.dto.response.AsyncTaskStatusResponse;
import com.example.cybersport_platform.dto.response.AsyncTasksOverviewResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AsyncBusinessTaskServiceImplTest {

    private final AsyncBusinessTaskServiceImpl service =
            new AsyncBusinessTaskServiceImpl(new AsyncTaskProcessor());

    @Test
    void startTaskShouldReturnTaskIdAndCompleteWithProgressDetails() {
        AsyncBusinessTaskRequest request = new AsyncBusinessTaskRequest("Generate report", 7_000);

        AsyncTaskStartResponse startResponse = service.startTask(request);

        assertThat(startResponse.getTaskId()).isPositive();
        assertThat(startResponse.getDurationMs()).isEqualTo(7_000);

        AsyncTaskStatusResponse finalStatus = service.getTaskStatus(startResponse.getTaskId());
        assertThat(finalStatus.getOperationName()).isEqualTo("Generate report");
        assertThat(finalStatus.getStatus()).isEqualTo(com.example.cybersport_platform.service.AsyncTaskExecutionStatus.COMPLETED);
        assertThat(finalStatus.getResult()).contains("Generate report");
        assertThat(finalStatus.getCompletedSteps()).isEqualTo(7);
        assertThat(finalStatus.getProgressPercent()).isEqualTo(100);
        assertThat(finalStatus.getRemainingMs()).isZero();
    }

    @Test
    void getTaskStatusShouldThrowWhenTaskMissing() {
        assertThatThrownBy(() -> service.getTaskStatus(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Task not found: 999");
    }

    @Test
    void getAllTaskStatusesShouldReturnSummaryAndTaskList() {
        service.startTask(new AsyncBusinessTaskRequest("Task one", 7_000));
        service.startTask(new AsyncBusinessTaskRequest("Task two", 8_000));

        AsyncTasksOverviewResponse response = service.getAllTaskStatuses();

        assertThat(response.getTotalTasks()).isEqualTo(2);
        assertThat(response.getCompletedTasks()).isEqualTo(2);
        assertThat(response.getPendingTasks()).isZero();
        assertThat(response.getRunningTasks()).isZero();
        assertThat(response.getFailedTasks()).isZero();
        assertThat(response.getTasks()).hasSize(2);
    }
}

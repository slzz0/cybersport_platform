package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.dto.request.AsyncBusinessTaskRequest;
import com.example.cybersport_platform.dto.response.AsyncTaskStartResponse;
import com.example.cybersport_platform.dto.response.AsyncTaskStatusResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AsyncBusinessTaskServiceImplTest {

    private final AsyncBusinessTaskServiceImpl service =
            new AsyncBusinessTaskServiceImpl(new AsyncTaskProcessor());

    @Test
    void startTaskShouldReturnTaskIdAndEventuallyComplete() throws InterruptedException {
        AsyncBusinessTaskRequest request = new AsyncBusinessTaskRequest("Generate report", 150);

        AsyncTaskStartResponse startResponse = service.startTask(request);

        assertThat(startResponse.getTaskId()).isPositive();
        AsyncTaskStatusResponse initialStatus = service.getTaskStatus(startResponse.getTaskId());
        assertThat(initialStatus.getOperationName()).isEqualTo("Generate report");

        Thread.sleep(250);

        AsyncTaskStatusResponse finalStatus = service.getTaskStatus(startResponse.getTaskId());
        assertThat(finalStatus.getStatus()).isEqualTo(com.example.cybersport_platform.service.AsyncTaskExecutionStatus.COMPLETED);
        assertThat(finalStatus.getResult()).contains("Generate report");
    }

    @Test
    void getTaskStatusShouldThrowWhenTaskMissing() {
        assertThatThrownBy(() -> service.getTaskStatus(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Task not found: 999");
    }
}

package com.example.cybersport_platform.dto.response;

import com.example.cybersport_platform.service.AsyncTaskExecutionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Async task start response")
public class AsyncTaskStartResponse {

    @Schema(description = "Task identifier", example = "1")
    private Long taskId;

    @Schema(description = "Current task status", example = "PENDING")
    private AsyncTaskExecutionStatus status;
}

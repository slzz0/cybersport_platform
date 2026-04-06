package com.example.cybersport_platform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to start an asynchronous business task")
public class AsyncBusinessTaskRequest {

    @Schema(description = "Business operation name", example = "Generate tournament report")
    @NotBlank(message = "Operation name is required")
    @Size(max = 120, message = "Operation name must be at most 120 characters")
    private String operationName;

    @Schema(description = "Simulated execution time in milliseconds", example = "3000")
    @Min(value = 100, message = "Duration must be at least 100 ms")
    @Max(value = 30000, message = "Duration must be at most 30000 ms")
    private long durationMs;
}

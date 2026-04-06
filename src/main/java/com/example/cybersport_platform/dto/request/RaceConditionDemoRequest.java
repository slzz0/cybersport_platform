package com.example.cybersport_platform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request for race condition demonstration")
public class RaceConditionDemoRequest {

    @Schema(description = "Thread count for ExecutorService demo", example = "64")
    @Min(value = 50, message = "Thread count must be at least 50")
    @Max(value = 500, message = "Thread count must be at most 500")
    private int threadCount;

    @Schema(description = "Increment count per thread", example = "2000")
    @Min(value = 1, message = "Increments per thread must be at least 1")
    @Max(value = 100000, message = "Increments per thread must be at most 100000")
    private int incrementsPerThread;
}

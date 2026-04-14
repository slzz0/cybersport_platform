package com.example.cybersport_platform.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Race condition demonstration result")
public class RaceConditionDemoResponse {

    @Schema(description = "ExecutorService thread count", example = "8")
    private int threadCount;

    @Schema(description = "Increment count per thread", example = "2000")
    private int incrementsPerThread;

    @Schema(description = "Expected final counter value", example = "128000")
    private int expectedValue;

    @Schema(description = "Actual final value produced by the unsafe counter", example = "117452")
    private int unsafeValue;

    @Schema(description = "Safe counter value using synchronized", example = "128000")
    private int synchronizedValue;

    @Schema(description = "Safe counter value using AtomicInteger", example = "128000")
    private int atomicValue;
}

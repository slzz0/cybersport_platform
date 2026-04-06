package com.example.cybersport_platform.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Thread-safe counter response")
public class CounterResponse {

    @Schema(description = "Current counter value", example = "42")
    private long value;
}

package com.example.cybersport_platform.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Unified API error response")
public record ApiError(
        @Schema(description = "HTTP status code", example = "400")
        int status,
        @Schema(description = "Error title", example = "Validation failed")
        String error,
        @Schema(description = "Detailed message", example = "Request contains invalid values")
        String message,
        @Schema(description = "Request path", example = "/api/matches")
        String path,
        @Schema(description = "Error timestamp", example = "2026-03-16T19:10:00")
        LocalDateTime timestamp,
        @Schema(description = "Validation details")
        List<ApiValidationError> validationErrors
) {
}

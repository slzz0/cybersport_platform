package com.example.cybersport_platform.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Field validation error")
public record ApiValidationError(
        @Schema(description = "Field name", example = "teamId")
        String field,
        @Schema(description = "Validation message", example = "Team id must be positive")
        String message
) {
}

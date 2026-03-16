package com.example.cybersport_platform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Team create/update request")
public class TeamRequest {

    @Schema(description = "Team name", example = "Team Spirit")
    @NotBlank(message = "Team name is required")
    @Size(max = 120, message = "Team name must be at most 120 characters")
    private String name;

    @Schema(description = "Related game id", example = "1")
    @NotNull(message = "Game id is required")
    @Positive(message = "Game id must be positive")
    private Long gameId;
}

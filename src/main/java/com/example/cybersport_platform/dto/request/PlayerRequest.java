package com.example.cybersport_platform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
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
@Schema(description = "Player create/update request")
public class PlayerRequest {

    @Schema(description = "Player nickname", example = "Collapse")
    @NotBlank(message = "Nickname is required")
    @Size(max = 80, message = "Nickname must be at most 80 characters")
    private String nickname;

    @Schema(description = "Player ELO", example = "3000")
    @NotNull(message = "ELO is required")
    @Min(value = 0, message = "ELO must be greater or equal to 0")
    private Integer elo;

    @Schema(description = "Related team id", example = "1")
    @NotNull(message = "Team id is required")
    @Positive(message = "Team id must be positive")
    private Long teamId;
}

package com.example.cybersport_platform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Game create/update request")
public class GameRequest {

    @Schema(description = "Game name", example = "Dota 2")
    @NotBlank(message = "Game name is required")
    @Size(max = 120, message = "Game name must be at most 120 characters")
    private String name;

    @Schema(description = "Game description", example = "MOBA esports title")
    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;
}

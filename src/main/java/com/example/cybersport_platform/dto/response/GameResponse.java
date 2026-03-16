package com.example.cybersport_platform.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Game response DTO")
public class GameResponse {

    @Schema(description = "Game id", example = "1")
    private Long id;

    @Schema(description = "Game name", example = "Dota 2")
    private String name;

    @Schema(description = "Game description", example = "MOBA esports title")
    private String description;
}

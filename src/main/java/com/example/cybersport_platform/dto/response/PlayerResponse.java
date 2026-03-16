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
@Schema(description = "Player response DTO")
public class PlayerResponse {

    @Schema(description = "Player id", example = "1")
    private Long id;

    @Schema(description = "Nickname", example = "Collapse")
    private String nickname;

    @Schema(description = "ELO", example = "3000")
    private Integer elo;

    @Schema(description = "Team id", example = "10")
    private Long teamId;

    @Schema(description = "Team name", example = "Team Spirit")
    private String teamName;
}

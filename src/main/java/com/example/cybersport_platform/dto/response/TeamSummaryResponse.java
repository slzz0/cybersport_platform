package com.example.cybersport_platform.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Compact team response DTO")
public class TeamSummaryResponse extends TeamBaseResponse {

    public TeamSummaryResponse(Long id, String name, Long gameId, String gameName) {
        super(id, name, gameId, gameName);
    }
}

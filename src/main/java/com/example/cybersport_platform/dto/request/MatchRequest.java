package com.example.cybersport_platform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class MatchRequest {
    private Long tournamentId;
    private Long team1Id;
    private Long team2Id;
    private Integer scoreTeam1;
    private Integer scoreTeam2;
    private LocalDateTime playedAt;
}

package com.example.cybersport_platform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerResponse {
    private Long id;
    private String nickname;
    private Integer elo;
    private Long teamId;
    private String teamName;
}

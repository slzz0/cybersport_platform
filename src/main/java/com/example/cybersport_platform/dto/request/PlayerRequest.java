package com.example.cybersport_platform.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRequest {
    private String nickname;
    private Integer elo;
    private Long teamId;
}

package com.example.cybersport_platform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponse {
    private Long id;
                private String name;
    private Long gameId;
    private String gameName;
}

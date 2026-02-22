package com.example.cybersport_platform.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameWithTeamsRequest {
    private String gameName;
    private String gameDescription;
    private List<String> teamNames;
    private boolean simulateError;
}

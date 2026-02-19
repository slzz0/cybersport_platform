package com.example.cybersport_platform.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {
    private Long id;
    private String nickname;
    private String team;
    private String game;
    private Integer elo;
}

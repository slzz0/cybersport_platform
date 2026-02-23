package com.example.cybersport_platform.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные игрока. Можно передать nickname или name — оба сохраняются как никнейм.")
public class PlayerRequest {
    @Schema(description = "Никнейм игрока (в JSON можно писать nickname или name)", example = "donk", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonAlias("name")
    private String nickname;

    @Schema(description = "Рейтинг ELO (число, необязательно)", example = "1500")
    private Integer elo;

    @Schema(description = "ID команды (число, необязательно). null = без команды", example = "1")
    private Long teamId;
}

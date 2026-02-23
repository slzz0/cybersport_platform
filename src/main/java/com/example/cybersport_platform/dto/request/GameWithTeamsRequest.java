package com.example.cybersport_platform.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Демо: создание игры и команд за один запрос")
public class GameWithTeamsRequest {
    @Schema(description = "Название игры (в JSON: gameName или name)", example = "CS2", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonAlias("name")
    private String gameName;

    @Schema(description = "Описание игры (в JSON: gameDescription или description)", example = "Counter-Strike 2")
    @JsonAlias("description")
    private String gameDescription;

    @Schema(description = "Список названий команд", example = "[\"NaVi\", \"Vitality\"]")
    private List<String> teamNames;

    @Schema(description = "Имитировать ошибку для демо отката транзакции", example = "false")
    private boolean simulateError;
}

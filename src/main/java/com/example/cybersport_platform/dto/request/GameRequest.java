package com.example.cybersport_platform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные игры")
public class GameRequest {
    @Schema(description = "Название игры", example = "CS2", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Описание игры", example = "Counter-Strike 2")
    private String description;
}

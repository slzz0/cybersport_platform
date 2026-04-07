package com.example.cybersport_platform.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cybersportOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cybersport Platform API")
                        .description("API for managing games, teams, tournaments players and matches")
                        .version("v1"));
    }
}

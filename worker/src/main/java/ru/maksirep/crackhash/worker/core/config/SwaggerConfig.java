package ru.maksirep.crackhash.worker.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private static final String TITLE = "Crack hash worker";

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info().title(TITLE));
    }
}
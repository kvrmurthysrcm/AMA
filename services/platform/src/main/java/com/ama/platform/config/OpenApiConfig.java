package com.ama.platform.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI amaOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("AMA Analytics API")
                        .version("v1")
                        .description("Amazon Marketplace Analytics platform MVP-1 endpoints")
                        .license(new License().name("Internal")));
    }
}

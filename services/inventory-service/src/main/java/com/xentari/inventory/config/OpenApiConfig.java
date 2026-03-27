package com.xentari.inventory.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventoryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Xentari Inventory Service API")
                        .description("Stock reservation and management with RabbitMQ event consumers")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Xentari Team")
                                .url("https://github.com/BilalR4M/xentari")));
    }
}

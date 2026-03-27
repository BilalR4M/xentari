package com.xentari.order.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orderOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Xentari Order Service API")
                        .description("Order lifecycle management with event-driven choreography saga")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Xentari Team")
                                .url("https://github.com/BilalR4M/xentari")));
    }
}

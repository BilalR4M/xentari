package com.xentari.product.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Xentari Product Service API")
                        .description("Product catalog management - create, read, update, delete products")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Xentari Team")
                                .url("https://github.com/BilalR4M/xentari")));
    }
}

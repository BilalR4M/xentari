package com.xentari.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ProductClient {

    private static final Logger log = LoggerFactory.getLogger(ProductClient.class);
    private static final String PRODUCT_SERVICE_URL = "http://product-service";

    private final RestClient restClient;

    public ProductClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(PRODUCT_SERVICE_URL).build();
    }

    public BigDecimal getProductPrice(Long productId) {
        try {
            Map<String, Object> response = restClient.get()
                    .uri("/api/products/{id}", productId)
                    .retrieve()
                    .body(Map.class);

            if (response != null && response.containsKey("price")) {
                return new BigDecimal(response.get("price").toString());
            }
        } catch (Exception e) {
            log.error("Failed to fetch product price for productId={}: {}", productId, e.getMessage());
        }
        throw new RuntimeException("Product not found: " + productId);
    }
}

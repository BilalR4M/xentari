package com.xentari.inventory.controller;

import com.xentari.inventory.entity.Inventory;
import com.xentari.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventory", description = "Stock reservation and management")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get inventory by product ID", description = "Returns stock levels and reserved quantity for a product")
    @ApiResponse(responseCode = "200", description = "Inventory found")
    @ApiResponse(responseCode = "404", description = "Product inventory not found")
    public ResponseEntity<?> getInventory(
            @Parameter(description = "Product ID") @PathVariable Long productId) {
        return inventoryService.getByProductId(productId)
                .map(inv -> ResponseEntity.ok(Map.of(
                        "productId", inv.getProductId(),
                        "quantity", inv.getQuantity(),
                        "reservedQuantity", inv.getReservedQuantity(),
                        "available", inv.getQuantity() - inv.getReservedQuantity()
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}

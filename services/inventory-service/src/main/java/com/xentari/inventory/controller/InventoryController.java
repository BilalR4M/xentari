package com.xentari.inventory.controller;

import com.xentari.inventory.entity.Inventory;
import com.xentari.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getInventory(@PathVariable Long productId) {
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

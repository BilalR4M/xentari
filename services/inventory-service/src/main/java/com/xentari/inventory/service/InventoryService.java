package com.xentari.inventory.service;

import com.xentari.inventory.entity.Inventory;
import com.xentari.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public boolean reserveStock(Long productId, int quantity) {
        Optional<Inventory> optional = inventoryRepository.findByProductId(productId);
        if (optional.isEmpty()) {
            return false;
        }
        Inventory inventory = optional.get();
        if (!inventory.canReserve(quantity)) {
            return false;
        }
        inventory.reserve(quantity);
        inventoryRepository.save(inventory);
        return true;
    }

    @Transactional
    public void releaseStock(Long productId, int quantity) {
        inventoryRepository.findByProductId(productId).ifPresent(inventory -> {
            inventory.releaseReservation(quantity);
            inventoryRepository.save(inventory);
        });
    }

    public Optional<Inventory> getByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }
}

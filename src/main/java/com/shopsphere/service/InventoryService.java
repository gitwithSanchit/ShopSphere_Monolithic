package com.shopsphere.service;

import com.shopsphere.model.Inventory;
import com.shopsphere.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    // 1. Initialize or Update Stock (Admin use)
    public Inventory updateStock(Long productId, Integer quantity, Integer threshold) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElse(new Inventory());

        inventory.setProductId(productId);
        inventory.setQuantityAvailable(quantity);
        if (threshold != null) {
            inventory.setLowStockThreshold(threshold);
        }

        return inventoryRepository.save(inventory);
    }

    // 2. Check and Deduct Stock (Used by Cart/Order Module)
    @Transactional
    public boolean deductStock(Long productId, Integer quantityToDeduct) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for Product ID: " + productId));

        if (inventory.getQuantityAvailable() >= quantityToDeduct) {
            inventory.setQuantityAvailable(inventory.getQuantityAvailable() - quantityToDeduct);
            inventoryRepository.save(inventory);

            // Log a warning if stock hits threshold (Simulating an alert)
            if (inventory.getQuantityAvailable() <= inventory.getLowStockThreshold()) {
                System.out.println("ALERT: Low stock for Product " + productId + ". Only " + inventory.getQuantityAvailable() + " left.");
            }
            return true;
        }
        return false; // Insufficient stock
    }

    // 3. Get Current Stock Info
    public Optional<Inventory> getStockDetails(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }
}
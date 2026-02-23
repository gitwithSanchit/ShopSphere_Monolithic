package com.shopsphere.controller;

import com.shopsphere.model.Inventory;
import com.shopsphere.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // 1. Update or Initialize Stock
    @PostMapping("/update")
    public ResponseEntity<Inventory> updateStock(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) Integer threshold) {

        return ResponseEntity.ok(inventoryService.updateStock(productId, quantity, threshold));
    }

    // 2. Get Stock Details for a Product
    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> getStock(@PathVariable Long productId) {
        Optional<Inventory> inventory = inventoryService.getStockDetails(productId);
        return inventory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. Test Stock Deduction (Simulating a purchase)
    @PostMapping("/deduct/{productId}")
    public ResponseEntity<String> deductStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        boolean success = inventoryService.deductStock(productId, quantity);
        if (success) {
            return ResponseEntity.ok("Stock deducted successfully");
        } else {
            return ResponseEntity.badRequest().body("Insufficient stock or product not found");
        }
    }
}
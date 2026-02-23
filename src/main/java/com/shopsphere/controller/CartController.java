package com.shopsphere.controller;

import com.shopsphere.model.CartItem;
import com.shopsphere.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // 1. Add Item to Cart
    @PostMapping("/add")
    public ResponseEntity<String> addItemToCart(
            @RequestParam Long cartId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        String result = cartService.addItemToCart(cartId, productId, quantity);

        if (result.contains("successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // 2. View All Items in Cart
    @GetMapping("/{cartId}")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.getCartItems(cartId));
    }

    // 3. Get Cart Total (Professional Addition)
    @GetMapping("/{cartId}/total")
    public ResponseEntity<BigDecimal> getCartTotal(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.calculateCartTotal(cartId));
    }

    // 4. Remove Single Item
    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<String> removeItem(@PathVariable Long cartItemId) {
        cartService.removeItemFromCart(cartItemId);
        return ResponseEntity.ok("Item removed from cart");
    }

    // 5. Clear Entire Cart
    @DeleteMapping("/clear/{cartId}")
    public ResponseEntity<String> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.ok("Cart cleared successfully");
    }
}
package com.shopsphere.service;

import com.shopsphere.model.CartItem;
import com.shopsphere.model.Inventory;
import com.shopsphere.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private InventoryService inventoryService;

    // Assuming you have a ProductService to fetch details like price
    @Autowired
    private ProductService productService;

    @Transactional
    public String addItemToCart(Long cartId, Long productId, Integer quantity) {
        // 1. Validate Inventory (The Gatekeeper)
        Optional<Inventory> inventory = inventoryService.getStockDetails(productId);
        if (inventory.isEmpty() || inventory.get().getQuantityAvailable() < quantity) {
            return "Insufficient stock! Only " +
                    (inventory.isPresent() ? inventory.get().getQuantityAvailable() : 0) +
                    " items left.";
        }

        // 2. Check if product already exists in this specific cart
        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cartId, productId);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            // 3. Fetch current product price to "lock" it in the CartItem
            BigDecimal currentPrice = productService.getProductById(productId).getPrice();

            CartItem newItem = new CartItem();
            newItem.setCartId(cartId);
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            newItem.setPrice(currentPrice);

            cartItemRepository.save(newItem);
        }

        return "Item added to cart successfully!";
    }

    public List<CartItem> getCartItems(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Transactional
    public void removeItemFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public void clearCart(Long cartId) {
        cartItemRepository.deleteByCartId(cartId);
    }

    public BigDecimal calculateCartTotal(Long cartId) {
        List<CartItem> items = cartItemRepository.findByCartId(cartId);

        return items.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
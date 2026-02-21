package com.shopsphere.controller;

import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 1. READ ALL
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // 2. READ ONE
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // 3. CREATE
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        // This takes JSON from Postman and saves it to DB
        return productService.saveOrUpdateProduct(product);
    }

    // 4. UPDATE
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        // Ensure we are updating the existing product ID
        product.setId(id);
        return productService.saveOrUpdateProduct(product);
    }

    // 5. DELETE
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product with ID " + id + " has been deleted successfully.";
    }

    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        return productService.searchProducts(name, categoryId, minPrice, maxPrice);
    }

    @GetMapping("/page")
    public Page<Product> getProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return productService.getProductsWithPagination(page, size, sortBy, direction);
    }
}
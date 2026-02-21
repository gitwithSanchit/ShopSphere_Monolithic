package com.shopsphere.service;

import com.shopsphere.model.Product;
import com.shopsphere.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Method to fetch all 10 products we added to the DB
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Method to find a specific product by its ID
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // CREATE or UPDATE
    public Product saveOrUpdateProduct(Product product) {
        return productRepository.save(product);
    }

    // DELETE
    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    //FOR ADVANCED SEARCH THIS ACTS AS A BRIDGE
    public List<Product> searchProducts(String name, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByAdvancedSearch(name, categoryId, minPrice, maxPrice);
    }

    public Page<Product> getProductsWithPagination(int page, int size, String sortBy, String direction) {
        // Determine sort direction (ASC or DESC)
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        // Create a Pageable object (page is 0-indexed)
        Pageable pageable = PageRequest.of(page, size, sort);

        // Return a 'Page' object which contains the data + metadata (total pages, etc.)
        return productRepository.findAll(pageable);
    }
}

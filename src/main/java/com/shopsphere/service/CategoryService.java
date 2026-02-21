package com.shopsphere.service;

import com.shopsphere.model.Category;
import com.shopsphere.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category saveOrUpdateCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> getCategoryTree() {
        // 1. Fetch all categories from DB
        List<Category> allCategories = categoryRepository.findAll();

        // 2. Filter to find only "Root" categories (where parentId is null)
        List<Category> rootCategories = allCategories.stream()
                .filter(c -> c.getParentId() == null)
                .collect(Collectors.toList());

        // 3. For each root, find its children recursively
        for (Category root : rootCategories) {
            fillChildren(root, allCategories);
        }

        return rootCategories;
    }

    private void fillChildren(Category parent, List<Category> allCategories) {
        List<Category> children = allCategories.stream()
                .filter(c -> parent.getId().equals(c.getParentId()))
                .collect(Collectors.toList());

        parent.setChildren(children);

        // Recurse for deeper levels (Grandchildren)
        for (Category child : children) {
            fillChildren(child, allCategories);
        }
    }
}
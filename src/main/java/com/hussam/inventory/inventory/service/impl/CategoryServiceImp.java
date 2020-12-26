package com.hussam.inventory.inventory.service.impl;

import com.hussam.inventory.inventory.entities.Category;
import com.hussam.inventory.inventory.repositories.CategoryRepository;
import com.hussam.inventory.inventory.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }
}

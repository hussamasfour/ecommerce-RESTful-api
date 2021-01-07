package com.hussam.inventory.inventory.service.impl;

import com.hussam.inventory.inventory.controllers.CartController;
import com.hussam.inventory.inventory.entities.Category;
import com.hussam.inventory.inventory.repositories.CategoryRepository;
import com.hussam.inventory.inventory.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImp implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImp.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category addCategory(Category category) {
        LOGGER.info("Saving the new added category");
        return categoryRepository.save(category);
    }
}

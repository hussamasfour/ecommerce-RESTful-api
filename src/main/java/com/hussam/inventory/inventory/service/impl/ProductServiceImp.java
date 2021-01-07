package com.hussam.inventory.inventory.service.impl;

import com.hussam.inventory.inventory.controllers.UserController;
import com.hussam.inventory.inventory.entities.Category;
import com.hussam.inventory.inventory.entities.Product;
import com.hussam.inventory.inventory.exception.NotFoundException;
import com.hussam.inventory.inventory.repositories.CategoryRepository;
import com.hussam.inventory.inventory.repositories.ProductRepository;
import com.hussam.inventory.inventory.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImp.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Optional<Product> getProductByIdAndCategory(Long id, String categoryName){
        LOGGER.info("fetching category for category name:" + categoryName);
        Optional<Category> category = categoryRepository.findCategoryByName(categoryName);
        if(!category.isPresent()){
            throw new NotFoundException("There is no category with this selected name " + categoryName);
        }
        LOGGER.info("Fetching product for id:" + id +" and Category:" + categoryName);
        return productRepository.findProductByCategoryAndId(category.get(),id);
    }

    @Override
    public Product add(Product product,String categoryName){
        LOGGER.info("Fetching category for category name:" + categoryName);
        Optional<Category> category = categoryRepository.findCategoryByName(categoryName);
        if(!category.isPresent()){
            throw new NotFoundException("There is no category with this selected name");
        }
        product.setCategory(category.get());
        product.setDateCreated(new Date());

        LOGGER.info("Saving Product:" + product );
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        LOGGER.info("Fetching product by id: " + id);
        return productRepository.findById(id);

    }

    @Override
    public List<Product> getAllByCategory(String categoryName) {
        LOGGER.info("Fetching category for category name:" + categoryName);
        Optional<Category> category = categoryRepository.findCategoryByName(categoryName);
        if(!category.isPresent()){
            throw new NotFoundException("There is no category with the selected name");
        }
        LOGGER.info("Fetching all product with selected Category:"+ categoryName);
        return productRepository.findAllByCategory(category.get());
    }

    @Override
    public Product update(Product product) {
        LOGGER.info("Updating product " + product);
        return productRepository.save(product);
    }
}

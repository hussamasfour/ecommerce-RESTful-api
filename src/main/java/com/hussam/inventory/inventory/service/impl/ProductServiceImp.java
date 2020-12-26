package com.hussam.inventory.inventory.service.impl;

import com.hussam.inventory.inventory.entities.Category;
import com.hussam.inventory.inventory.entities.Product;
import com.hussam.inventory.inventory.exception.NotFoundException;
import com.hussam.inventory.inventory.repositories.CategoryRepository;
import com.hussam.inventory.inventory.repositories.ProductRepository;
import com.hussam.inventory.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Optional<Product> getProductByIdAndCategory(Long id, String categoryName){
        Optional<Category> category = categoryRepository.findCategoryByName(categoryName);
        if(!category.isPresent()){
            throw new NotFoundException("Category not correct");
        }
        return productRepository.findProductByCategoryAndId(category.get(),id);
    }

    @Override
    public Product add(Product product,String category){
        Optional<Category> category1 = categoryRepository.findCategoryByName(category);
        if(!category1.isPresent()){
            throw new NotFoundException("please enter a valid category name");
        }
        product.setCategory(category1.get());
        product.setDateCreated(new Date());
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {

        Optional<Product> product = productRepository.findById(id);

        return product;
    }

    @Override
    public List<Product> getAllByCategory( String categoryName) {
        Optional<Category> category = categoryRepository.findCategoryByName(categoryName);
        if(!category.isPresent()){
            throw new NotFoundException("Please enter a valid category name");
        }
        return productRepository.findAllByCategory(category.get());
    }

    @Override
    public Product update(Product product) {
        return productRepository.save(product);
    }
}

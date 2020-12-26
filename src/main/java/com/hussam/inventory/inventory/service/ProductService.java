package com.hussam.inventory.inventory.service;

import com.hussam.inventory.inventory.entities.Product;

import java.util.List;
import java.util.Optional;


public interface ProductService {

    public Optional<Product> getProductByIdAndCategory(Long id, String categoryName);

    public Product add(Product product, String category);

    public Optional<Product> getProductById(Long id);

    public List<Product> getAllByCategory(String categoryName);

    public Product update(Product product);
}

package com.hussam.inventory.inventory.repositories;

import com.hussam.inventory.inventory.entities.Category;
import com.hussam.inventory.inventory.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findProductByCategoryAndId(Category category, Long productId);

    List<Product> findAllByCategory(Category category);

    Optional<Product> findProductsByIdAndCategory(Long id, Category category);
}

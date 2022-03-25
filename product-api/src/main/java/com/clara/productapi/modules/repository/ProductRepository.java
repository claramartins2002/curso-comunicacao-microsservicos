package com.clara.productapi.modules.repository;

import com.clara.productapi.modules.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByNameIgnoreCaseContaining(String name);
    List<Product> findBySupplierId (Integer id);
    List<Product> findByCategoryId (Integer id);
    Boolean existsByCategoryId(Integer id);
    Boolean existsBySupplierId(Integer id);
}

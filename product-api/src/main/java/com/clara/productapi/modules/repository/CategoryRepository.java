package com.clara.productapi.modules.repository;

import com.clara.productapi.modules.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository <Category, Integer> {
    List<Category> findByDescriptionIgnoreCaseContaining(String description);
}

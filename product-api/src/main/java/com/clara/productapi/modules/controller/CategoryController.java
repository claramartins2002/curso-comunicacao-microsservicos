package com.clara.productapi.modules.controller;

import com.clara.productapi.config.exception.SucessResponse;
import com.clara.productapi.modules.dto.CategoryRequest;
import com.clara.productapi.modules.dto.CategoryResponse;
import com.clara.productapi.modules.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public CategoryResponse save(@RequestBody CategoryRequest category) throws ValidationException {
        return categoryService.save(category);
    }
    @GetMapping
    public List<CategoryResponse> findAll() {
        return categoryService.findAll();
    }
    @GetMapping("{id}")
    public CategoryResponse findById(@PathVariable Integer id) throws ValidationException {
        return categoryService.findByIdRes(id);
    }
    @GetMapping("description/{description}")
    public List<CategoryResponse> findByDescription(@PathVariable String description) throws ValidationException {
        return categoryService.findByDescription(description);
    }
    @DeleteMapping("{id}")
    public SucessResponse delete(@PathVariable Integer id) throws ValidationException {
        return categoryService.delete(id);
    }
    @PutMapping("{id}")
    public CategoryResponse update(@PathVariable Integer id, @RequestBody CategoryRequest request) throws ValidationException {
        return categoryService.update(request, id);
    }
}

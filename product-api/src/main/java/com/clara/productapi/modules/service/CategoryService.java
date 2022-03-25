package com.clara.productapi.modules.service;

import com.clara.productapi.config.exception.SucessResponse;
import com.clara.productapi.modules.dto.CategoryRequest;
import com.clara.productapi.modules.dto.CategoryResponse;
import com.clara.productapi.modules.model.Category;
import com.clara.productapi.modules.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;
    @Autowired @Lazy
    private ProductService productService;

    public CategoryResponse save(CategoryRequest request) throws ValidationException {
        validateCategoryNameInformed(request);
        var category = repository.save(Category.of(request));
        return CategoryResponse.of(category);
    }
    public CategoryResponse update(CategoryRequest request, Integer id) throws ValidationException {
        validateCategoryNameInformed(request);
        validateInformedId(id);
        var category = Category.of(request);
        category.setId(id);
        repository.save(category);
        return CategoryResponse.of(category);
    }
    private void validateCategoryNameInformed(CategoryRequest request) throws ValidationException {
        if((request.getDescription()).isEmpty()){
            throw new ValidationException("The category description was not informed!!!!!");
        }
    }
    public List<CategoryResponse> findAll(){
        return repository.findAll().stream().map(CategoryResponse::of).collect(Collectors.toList());
    }

    public Category findById(Integer id) throws ValidationException {
        return repository.findById(id).orElseThrow();
    }
    public CategoryResponse findByIdRes(Integer id) throws ValidationException {
        return repository.findById(id).map(CategoryResponse::of).orElseThrow();
    }

    public List<CategoryResponse> findByDescription(String description) throws ValidationException {
        return repository.findByDescriptionIgnoreCaseContaining(description).stream().map(CategoryResponse::of).collect(Collectors.toList());
    }

    public SucessResponse delete(Integer id) throws ValidationException {
        validateInformedId(id);
        if(productService.existsBySupplierId(id)){
            throw new ValidationException("You cannot delete this category because it is already defined by a product!!");
        }
        repository.deleteById(id);
        return SucessResponse.create("The category was deleted!");
    }

    private void validateInformedId(Integer id) throws ValidationException {
        if(isEmpty(id)){
            throw new ValidationException("The category id must be informed!!!!");
        }
    }
}

package com.clara.productapi.modules.controller;

import com.clara.productapi.config.exception.SucessResponse;
import com.clara.productapi.modules.dto.CategoryRequest;
import com.clara.productapi.modules.dto.CategoryResponse;
import com.clara.productapi.modules.dto.ProductRequest;
import com.clara.productapi.modules.dto.ProductResponse;
import com.clara.productapi.modules.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @PostMapping
    public ProductResponse save(@RequestBody ProductRequest request) throws ValidationException {
        return productService.save(request);
    }
    @GetMapping
    public List<ProductResponse> findAll() {
        return productService.findAll();
    }
    @GetMapping("{id}")
    public ProductResponse findById(@PathVariable Integer id) throws ValidationException {
        return productService.findByIdRes(id);
    }
    @GetMapping("name/{name}")
    public List<ProductResponse> findByDescription(@PathVariable String name) throws ValidationException {
        return productService.findByDescription(name);
    }
    @GetMapping("supplier/{supplierId}")
    public List<ProductResponse> findBySupplierId(@PathVariable Integer supplierId) throws ValidationException {
        return productService.findBySupplierId(supplierId);
    }
    @GetMapping("category/{categoryId}")
    public List<ProductResponse> findByCategoryId(@PathVariable Integer categoryId) throws ValidationException {
        return productService.findByCategoryId(categoryId);
    }
    @DeleteMapping("{id}")
    public SucessResponse delete(@PathVariable Integer id) throws ValidationException {
        return productService.delete(id);
    }
    @PutMapping("{id}")
    public ProductResponse update(@PathVariable Integer id, @RequestBody ProductRequest request) throws ValidationException {
        return productService.update(request, id);
    }
}


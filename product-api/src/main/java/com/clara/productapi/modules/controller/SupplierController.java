package com.clara.productapi.modules.controller;

import com.clara.productapi.config.exception.SucessResponse;
import com.clara.productapi.modules.dto.CategoryRequest;
import com.clara.productapi.modules.dto.CategoryResponse;
import com.clara.productapi.modules.dto.SupplierRequest;
import com.clara.productapi.modules.dto.SupplierResponse;
import com.clara.productapi.modules.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {
    @Autowired
    private SupplierService service;

    @PostMapping
    public SupplierResponse save(@RequestBody SupplierRequest supplier) throws ValidationException {
        return service.save(supplier);
    }
    @GetMapping
    public List<SupplierResponse> findAll() {
        return service.findAll();
    }
    @GetMapping("{id}")
    public SupplierResponse findById(@PathVariable Integer id) throws ValidationException {
        return service.findByIdRes(id);
    }
    @GetMapping("name/{name}")
    public List<SupplierResponse> findByDescription(@PathVariable String name) throws ValidationException {
        return service.findByDescription(name);
    }
    @DeleteMapping("{id}")
    public SucessResponse delete(@PathVariable Integer id) throws ValidationException {
        return service.delete(id);
    }
    @PutMapping("{id}")
    public SupplierResponse update(@PathVariable Integer id, @RequestBody SupplierRequest request) throws ValidationException {
        return service.update(request, id);
    }
}

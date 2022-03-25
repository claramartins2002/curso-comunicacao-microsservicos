package com.clara.productapi.modules.service;

import com.clara.productapi.config.exception.SucessResponse;
import com.clara.productapi.modules.dto.CategoryRequest;
import com.clara.productapi.modules.dto.CategoryResponse;
import com.clara.productapi.modules.dto.SupplierRequest;
import com.clara.productapi.modules.dto.SupplierResponse;
import com.clara.productapi.modules.model.Category;
import com.clara.productapi.modules.model.Supplier;
import com.clara.productapi.modules.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository repository;

    @Autowired @Lazy
    private ProductService productService;

    public SupplierResponse save(SupplierRequest request) throws ValidationException {
        validateSupplierNameInformed(request);
        var supplier = repository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }
    public SupplierResponse update(SupplierRequest request, Integer id) throws ValidationException {
        validateSupplierNameInformed(request);
        validateInformedId(id);
        var supplier = Supplier.of(request);
        supplier.setId(id);
        repository.save(supplier);
        return SupplierResponse.of(supplier);
    }

    private void validateSupplierNameInformed(SupplierRequest request) throws ValidationException {
        if((request.getName()).isEmpty()){
            throw new ValidationException("The supplier name was not informed!!!!!");
        }
    }

    public Supplier findById(Integer id) throws ValidationException {
        return repository.findById(id).orElseThrow(()-> new ValidationException("There's no supplier for the given ID!!!"));
    }

    public List<SupplierResponse> findAll(){
        return repository.findAll().stream().map(SupplierResponse::of).collect(Collectors.toList());
    }

    public SupplierResponse findByIdRes(Integer id) throws ValidationException {
        return repository.findById(id).map(SupplierResponse::of).orElseThrow();
    }

    public List<SupplierResponse> findByDescription(String description) throws ValidationException {
        return repository.findByNameIgnoreCaseContaining(description).stream().map(SupplierResponse::of).collect(Collectors.toList());
    }

    public SucessResponse delete(Integer id) throws ValidationException {
       validateInformedId(id);
       if(productService.existsBySupplierId(id)){
           throw new ValidationException("You cannot delete this supplier because it is already defined by a product!!");
       }
       repository.deleteById(id);
       return SucessResponse.create("The supplier was deleted!");
    }

    private void validateInformedId(Integer id) throws ValidationException {
        if(isEmpty(id)){
            throw new ValidationException("The supplier id must be informed!!!!");
        }
    }
}

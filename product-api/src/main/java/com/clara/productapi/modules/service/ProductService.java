package com.clara.productapi.modules.service;

import com.clara.productapi.config.exception.SucessResponse;
import com.clara.productapi.modules.client.SalesClient;
import com.clara.productapi.modules.dto.*;
import com.clara.productapi.modules.enums.SalesStatus;
import com.clara.productapi.modules.model.Category;
import com.clara.productapi.modules.model.Product;
import com.clara.productapi.modules.model.Supplier;
import com.clara.productapi.modules.rabbit.SalesConfirmationSender;
import com.clara.productapi.modules.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SalesConfirmationSender salesConfirmationSender;
    @Autowired
    private SalesClient salesClient;

    public ProductResponse save(ProductRequest request) throws ValidationException {
        validateProductDataInformed(request);
        validateCategoryAndSupplierInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = productRepository.save(Product.of(request, supplier, category));
        return ProductResponse.of(product);
    }
    public ProductResponse update(ProductRequest request, Integer id) throws ValidationException {
        validateProductDataInformed(request);
        validateCategoryAndSupplierInformed(request);
        validateInformedId(id);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = Product.of(request, supplier, category);
        product.setId(id);
        productRepository.save(product);
        return ProductResponse.of(product);
    }


    private void validateProductDataInformed(ProductRequest request) throws ValidationException {
        if((request.getName()).isEmpty()){
            throw new ValidationException("The product name was not informed!!!!!");
        }
        if((request.getQuantity())==null||request.getQuantity()<0){
            throw new ValidationException("The product quantity can't be null or negative!!!!");
        }
    }

    private void validateCategoryAndSupplierInformed(ProductRequest request) throws ValidationException {
        if(request.getCategoryId()==null){
            throw new ValidationException("The category ID was not informed!!!!!");
        }
        if(request.getSupplierId()==null){
            throw new ValidationException("The supplier ID was not informed!!!!!");
        }
    }
    public List<ProductResponse> findAll(){
        return productRepository.findAll().stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public Product findById(Integer id) throws ValidationException {
        return productRepository.findById(id).orElseThrow();
    }
    public ProductResponse findByIdRes(Integer id) throws ValidationException {
        return productRepository.findById(id).map(ProductResponse::of).orElseThrow();
    }

    public List<ProductResponse> findByDescription(String description) throws ValidationException {
        return productRepository.findByNameIgnoreCaseContaining(description).stream().map(ProductResponse::of).collect(Collectors.toList());
    }
    public List<ProductResponse> findBySupplierId(Integer id) throws ValidationException {
        return productRepository.findBySupplierId(id).stream().map(ProductResponse::of).collect(Collectors.toList());
    }
    public List<ProductResponse> findByCategoryId(Integer id) throws ValidationException {
        return productRepository.findByCategoryId(id).stream().map(ProductResponse::of).collect(Collectors.toList());
    }
    public Boolean existsBySupplierId(Integer id) throws ValidationException {
        return productRepository.existsBySupplierId(id);
    }
    public Boolean existsByCategoryId(Integer id) throws ValidationException {
        return productRepository.existsByCategoryId(id);
    }

    public SucessResponse delete(Integer id) throws ValidationException {
        validateInformedId(id);
        productRepository.deleteById(id);
        return SucessResponse.create("The product was deleted!!");
    }

    private void validateInformedId(Integer id) throws ValidationException {
        if(isEmpty(id)){
            throw new ValidationException("The product id must be informed!!!!");
        }
    }
    public void updateProductStock(ProductStockDto product){
        var productsForUpdate = new ArrayList<Product>();
        try{
            validateStockUpdateData(product);
            product
                    .getProducts()
                    .forEach(
                            salesProduct -> {
                                try {
                                    var existingProduct = findById(salesProduct.getProductId());
                                    if(salesProduct.getQuantity() > existingProduct.getQuantity()){
                                        throw new ValidationException((String.format("The product %s is out of stock. ", existingProduct.getId())));
                                    }
                                    existingProduct.updateStock(salesProduct.getQuantity());
                                    productsForUpdate.add(existingProduct);

                                } catch (ValidationException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
            if(!isEmpty(productsForUpdate)) {
                productRepository.saveAll(productsForUpdate);
                var approvedSale = new SalesConfirmationDto(product.getSaleId(), SalesStatus.APPROVED);
                salesConfirmationSender.sendSalesConfirmationMessage(approvedSale);
            }
        }
        catch(Exception e){
            log.error("Error while trying to update stock for message with error {}", e.getMessage(), e);
            salesConfirmationSender.sendSalesConfirmationMessage(new SalesConfirmationDto(product.getSaleId(), SalesStatus.REJECTED
            ));
        }
    }
@Transactional
    private void validateStockUpdateData(ProductStockDto product) throws ValidationException {
        if (isEmpty(product)||isEmpty(product.getSaleId())) {
            throw new ValidationException("The product data or sales Id cannot be null!!!!!");
        }
        if(isEmpty(product.getProducts())){
            throw new ValidationException("The sales products must be informed");
        }
        product.getProducts()
                .forEach(salesProduct -> {
                    if(isEmpty(salesProduct.getQuantity())||isEmpty(salesProduct.getProductId())){
                        try {
                            throw new ValidationException("The product ID and the quantity must be informed!");
                        } catch (ValidationException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }
    public ProductSalesResponse findSalesProduct(Integer id) throws ValidationException {
        var product = findById(id);
        try {
            var sales = salesClient.findSalesByProductId(id).orElseThrow(()-> new ValidationException("The sales was not found this product)")) ;
            return ProductSalesResponse.of(product, sales.getSalesIds());
        }
        catch (Exception e){
            throw  new ValidationException("There was an error trying to get the product sales");
        }
    }

    public SucessResponse checkProductStock(ProductCheckStockRequest request) throws ValidationException {
        if(isEmpty(request)||isEmpty(request.getProducts())){
            throw new ValidationException("The request data must be informed!!!!!");
        }
        for (ProductQuantityDto productQuantityDto : request.getProducts()) {
            validateStock(productQuantityDto);
        }
        return SucessResponse.create("The stock is ok!!!!!");
    }

    private void validateStock(ProductQuantityDto product) throws ValidationException{
        if(isEmpty(product) || isEmpty(product.getQuantity())){
            throw new ValidationException("The product must be informed!!");
        }
        var productFind = findById(product.getProductId());
        if(productFind.getQuantity() < product.getQuantity()){
            throw new ValidationException("The product %s is out of stock", String.valueOf(product.getProductId()));
        }
    }
}

package com.clara.productapi.modules.model;

import com.clara.productapi.modules.dto.ProductRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "product")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "fk_category", nullable = false)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "fk_supplier", nullable = false)
    private Supplier supplier;
    private Integer quantity;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }

    public static Product of(ProductRequest request, Supplier supplier, Category category){
        return Product.builder()
                .name(request.getName())
                .quantity(request.getQuantity())
                .category(category)
                .supplier(supplier)
                .build();
    }
    public void updateStock(Integer quantityToSale){
        quantity = quantity - quantityToSale;
    }

}

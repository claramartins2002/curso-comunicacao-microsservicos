package com.clara.productapi.modules.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private Integer quantity;
    private Integer supplierId;
    private Integer categoryId;
}

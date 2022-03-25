package com.clara.productapi.modules.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStockDto implements Serializable {
    private String saleId;
    private List<ProductQuantityDto> products;

}

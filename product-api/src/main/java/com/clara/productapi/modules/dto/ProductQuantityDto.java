package com.clara.productapi.modules.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductQuantityDto implements Serializable {
    private Integer productId;
    private Integer quantity;

}

package com.clara.productapi.modules.dto;

import com.clara.productapi.modules.model.Supplier;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data

public class SupplierResponse {
    private Integer id;
    private String name;
    public static SupplierResponse of(Supplier supplier){
        var response = new SupplierResponse();
        BeanUtils.copyProperties(supplier, response);
        return response;
    }

}

package com.clara.productapi.modules.dto;

import com.clara.productapi.modules.enums.SalesStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesConfirmationDto {
    private String salesId;
    private SalesStatus status;
}

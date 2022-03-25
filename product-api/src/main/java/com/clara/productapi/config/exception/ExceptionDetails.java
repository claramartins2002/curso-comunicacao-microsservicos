package com.clara.productapi.config.exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ExceptionDetails {
    private int status;
    private String message;
}

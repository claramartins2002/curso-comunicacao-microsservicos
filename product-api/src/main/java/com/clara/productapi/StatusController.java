package com.clara.productapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/product")
public class StatusController {
    @GetMapping
    public ResponseEntity<HashMap<String, Object>> teste() {
        var response = new HashMap<String, Object>();
        response.put("service", "Product-API");
        response.put("status", "up");
        response.put("httpStatus", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }
}

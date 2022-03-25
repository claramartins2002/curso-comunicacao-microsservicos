package com.clara.productapi.modules.client;

import com.clara.productapi.modules.dto.SalesProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "salesClient", contextId = "salesClient", url = "${app-config.services.sales}")
public interface SalesClient {
    @GetMapping("products/{productId}")
    Optional<SalesProductResponse> findSalesByProductId(@PathVariable Integer productId);
}

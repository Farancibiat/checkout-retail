package com.retail.checkout.controller;

import com.retail.checkout.dto.CartCheckoutRequest;
import com.retail.checkout.dto.CheckoutResponse;
import com.retail.checkout.dto.ProductCatalogEntryDto;
import com.retail.checkout.service.CatalogService;
import com.retail.checkout.service.CheckoutService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final CatalogService catalogService;

    public CheckoutController(CheckoutService checkoutService, CatalogService catalogService) {
        this.checkoutService = checkoutService;
        this.catalogService = catalogService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductCatalogEntryDto>> getProducts() {
        return ResponseEntity.ok(catalogService.getProductsWithPromotions());
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(@Valid @RequestBody CartCheckoutRequest request) {
        CheckoutResponse response = checkoutService.processCheckout(request);
        return ResponseEntity.ok(response);
    }
}

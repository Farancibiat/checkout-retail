package com.retail.checkout.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductEntry(
        String sku,
        String name,
        String price,
        List<PromotionConfig> promotions
) {
    public List<PromotionConfig> getPromotions() {
        return promotions != null ? promotions : List.of();
    }

    public BigDecimal getPriceAsBigDecimal() {
        return price != null && !price.isBlank() ? new BigDecimal(price.trim()) : BigDecimal.ZERO;
    }
}

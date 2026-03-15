package com.retail.checkout.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PromotionConfig(
        String type,
        Integer minQuantity,
        BigDecimal percent
) {
    public static final String TYPE_QUANTITY_DISCOUNT = "quantity_discount";

    public int getMinQuantityOrDefault() {
        return minQuantity != null ? minQuantity : 0;
    }

    public BigDecimal getPercentOrDefault() {
        return percent != null ? percent : BigDecimal.ZERO;
    }
}

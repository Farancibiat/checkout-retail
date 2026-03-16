package com.retail.checkout.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

public record DiscountAppliedDto(
        String type,
        String description,
        BigDecimal amount,
        @JsonInclude(JsonInclude.Include.NON_NULL) String sku
) {}

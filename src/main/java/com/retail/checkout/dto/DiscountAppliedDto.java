package com.retail.checkout.dto;

import java.math.BigDecimal;

public record DiscountAppliedDto(
        String type,
        String description,
        BigDecimal amount
) {}

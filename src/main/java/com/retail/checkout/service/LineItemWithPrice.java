package com.retail.checkout.service;

import java.math.BigDecimal;

public record LineItemWithPrice(
        String sku,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal
) {}

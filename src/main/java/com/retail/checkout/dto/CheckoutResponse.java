package com.retail.checkout.dto;

import java.math.BigDecimal;
import java.util.List;

public record CheckoutResponse(
        String cartId,
        BigDecimal subtotal,
        List<DiscountAppliedDto> discounts,
        BigDecimal total,
        PaymentConfirmationDto payment,
        String paymentMethod
) {}

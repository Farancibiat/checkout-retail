package com.retail.checkout.service;

import java.math.BigDecimal;
import java.util.List;

public record CheckoutContext(
        String cartId,
        List<LineItemWithPrice> lineItems,
        BigDecimal subtotal,
        String paymentMethodId
) {}

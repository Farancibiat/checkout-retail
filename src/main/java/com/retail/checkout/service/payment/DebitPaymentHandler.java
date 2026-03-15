package com.retail.checkout.service.payment;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class DebitPaymentHandler implements PaymentMethodHandler {

    private static final String METHOD_ID = "DEBITO";
    private static final BigDecimal DISCOUNT_PERCENT = new BigDecimal("10");

    @Override
    public boolean supports(String paymentMethodId) {
        return METHOD_ID.equalsIgnoreCase(paymentMethodId);
    }

    @Override
    public BigDecimal getDiscountPercentage() {
        return DISCOUNT_PERCENT;
    }

    @Override
    public String simulateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}

package com.retail.checkout.service.payment;

import java.math.BigDecimal;

public interface PaymentMethodHandler {

    boolean supports(String paymentMethodId);

    BigDecimal getDiscountPercentage();

    String simulateTransactionId();
}

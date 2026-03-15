package com.retail.checkout.dto;

public record PaymentConfirmationDto(
        boolean paymentConfirmed,
        String transactionId
) {}

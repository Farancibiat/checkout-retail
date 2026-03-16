package com.retail.checkout.service;

import com.retail.checkout.domain.Product;
import com.retail.checkout.dto.*;
import com.retail.checkout.repository.ProductRepository;
import com.retail.checkout.service.payment.PaymentMethodHandler;
import com.retail.checkout.service.promotion.Promotion;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheckoutService {

    private final ProductRepository productRepository;
    private final List<Promotion> promotions;
    private final List<PaymentMethodHandler> paymentHandlers;

    public CheckoutService(ProductRepository productRepository,
                           List<Promotion> promotions,
                           List<PaymentMethodHandler> paymentHandlers) {
        this.productRepository = productRepository;
        this.promotions = promotions != null ? promotions : List.of();
        this.paymentHandlers = paymentHandlers != null ? paymentHandlers : List.of();
    }

    public CheckoutResponse processCheckout(CartCheckoutRequest request) {
        List<LineItemWithPrice> lineItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        for (var item : request.items()) {
            Product product = productRepository.findBySku(item.sku())
                    .orElseThrow(() -> new IllegalArgumentException("SKU no encontrado: " + item.sku()));
            BigDecimal lineTotal = product.price().multiply(BigDecimal.valueOf(item.quantity())).setScale(2, RoundingMode.HALF_UP);
            lineItems.add(new LineItemWithPrice(item.sku(), item.quantity(), product.price(), lineTotal));
            subtotal = subtotal.add(lineTotal);
        }

        CheckoutContext context = new CheckoutContext(
                request.cartId(),
                lineItems,
                subtotal,
                request.paymentMethod()
        );

        BigDecimal totalPromotionDiscounts = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        for (Promotion promotion : promotions) {
            List<DiscountAppliedDto> applied = promotion.apply(context);
            for (DiscountAppliedDto d : applied) {
                totalPromotionDiscounts = totalPromotionDiscounts.add(d.amount());
            }
        }

        List<DiscountAppliedDto> allDiscounts = new ArrayList<>();
        allDiscounts.add(new DiscountAppliedDto("PROMOTION", totalPromotionDiscounts, null));

        BigDecimal totalAfterPromotions = subtotal.subtract(totalPromotionDiscounts).setScale(2, RoundingMode.HALF_UP);

        PaymentMethodHandler paymentHandler = paymentHandlers.stream()
                .filter(h -> h.supports(request.paymentMethod()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Medio de pago no soportado: " + request.paymentMethod()));

        BigDecimal paymentDiscount = totalAfterPromotions
                .multiply(paymentHandler.getDiscountPercentage())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        allDiscounts.add(new DiscountAppliedDto(
                "PAYMENT",
                paymentDiscount,
                paymentHandler.getDiscountPercentage()
        ));

        BigDecimal total = totalAfterPromotions.subtract(paymentDiscount).setScale(2, RoundingMode.HALF_UP);
        String transactionId = paymentHandler.simulateTransactionId();

        return new CheckoutResponse(
                request.cartId(),
                subtotal,
                allDiscounts,
                total,
                new PaymentConfirmationDto(true, transactionId),
                request.paymentMethod()
        );
    }
}

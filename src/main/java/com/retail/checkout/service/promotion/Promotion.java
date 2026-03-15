package com.retail.checkout.service.promotion;

import com.retail.checkout.dto.DiscountAppliedDto;
import com.retail.checkout.dto.PromotionSummaryDto;
import com.retail.checkout.service.CheckoutContext;

import java.util.List;
import java.util.Optional;

public interface Promotion {

    List<DiscountAppliedDto> apply(CheckoutContext context);

    default Optional<PromotionSummaryDto> getPromotionInfoForSku(String sku) {
        return Optional.empty();
    }
}

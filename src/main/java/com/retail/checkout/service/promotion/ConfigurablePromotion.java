package com.retail.checkout.service.promotion;

import com.retail.checkout.config.CatalogLoader;
import com.retail.checkout.config.PromotionConfig;
import com.retail.checkout.dto.DiscountAppliedDto;
import com.retail.checkout.dto.PromotionSummaryDto;
import com.retail.checkout.service.CheckoutContext;
import com.retail.checkout.service.LineItemWithPrice;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ConfigurablePromotion implements Promotion {

    private final CatalogLoader catalogLoader;

    public ConfigurablePromotion(CatalogLoader catalogLoader) {
        this.catalogLoader = catalogLoader;
    }

    @Override
    public List<DiscountAppliedDto> apply(CheckoutContext context) {
        List<DiscountAppliedDto> result = new ArrayList<>();
        for (LineItemWithPrice line : context.lineItems()) {
            List<PromotionConfig> configs = catalogLoader.getPromotionConfigs(line.sku());
            for (PromotionConfig config : configs) {
                if (PromotionConfig.TYPE_QUANTITY_DISCOUNT.equals(config.type())) {
                    int minQty = config.getMinQuantityOrDefault();
                    if (line.quantity() >= minQty) {
                        BigDecimal percent = config.getPercentOrDefault();
                        BigDecimal discountAmount = line.lineTotal()
                                .multiply(percent)
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                        result.add(new DiscountAppliedDto("PROMOTION", discountAmount, percent));
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Optional<PromotionSummaryDto> getPromotionInfoForSku(String sku) {
        List<PromotionConfig> configs = catalogLoader.getPromotionConfigs(sku);
        if (configs.isEmpty()) {
            return Optional.empty();
        }
        List<PromotionSummaryDto> summaries = configs.stream()
                .map(ConfigurablePromotion::configToSummary)
                .collect(Collectors.toList());
        return summaries.isEmpty() ? Optional.empty() : Optional.of(summaries.get(0));
    }

    private static PromotionSummaryDto configToSummary(PromotionConfig config) {
        return new PromotionSummaryDto(
                config.type(),
                config.getMinQuantityOrDefault(),
                config.getPercentOrDefault()
        );
    }
}

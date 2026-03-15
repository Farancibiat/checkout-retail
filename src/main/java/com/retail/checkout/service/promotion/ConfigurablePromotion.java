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
                        String description = buildQuantityDiscountDescription(line.sku(), config);
                        result.add(new DiscountAppliedDto("PROMOTION", description, discountAmount));
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
                .map(c -> configToSummary(sku, c))
                .collect(Collectors.toList());
        return summaries.isEmpty() ? Optional.empty() : Optional.of(summaries.get(0));
    }

    private PromotionSummaryDto configToSummary(String sku, PromotionConfig config) {
        if (PromotionConfig.TYPE_QUANTITY_DISCOUNT.equals(config.type())) {
            String description = buildQuantityDiscountDescription(sku, config);
            return new PromotionSummaryDto("PROMOTION", description);
        }
        return new PromotionSummaryDto(config.type(), config.type());
    }

    private static String buildQuantityDiscountDescription(String sku, PromotionConfig config) {
        int minQty = config.getMinQuantityOrDefault();
        BigDecimal percent = config.getPercentOrDefault();
        return percent + "% dcto. por " + minQty + "+ unidades de " + sku;
    }
}

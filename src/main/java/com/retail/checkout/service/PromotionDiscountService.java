package com.retail.checkout.service;

import com.retail.checkout.config.CatalogLoader;
import com.retail.checkout.config.PromotionConfig;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class PromotionDiscountService {

    private final CatalogLoader catalogLoader;

    public PromotionDiscountService(CatalogLoader catalogLoader) {
        this.catalogLoader = catalogLoader;
    }

    /**
     * Calcula el total de descuentos por promociones de producto usando el catálogo.
     * Regla: quantity_discount con quantity >= minQuantity → lineTotal * percent / 100.
     */
    public BigDecimal computeTotalPromotionDiscount(List<LineItemWithPrice> lineItems) {
        BigDecimal total = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        for (LineItemWithPrice line : lineItems) {
            List<PromotionConfig> configs = catalogLoader.getPromotionConfigs(line.sku());
            for (PromotionConfig config : configs) {
                if (PromotionConfig.TYPE_QUANTITY_DISCOUNT.equals(config.type())
                        && line.quantity() >= config.getMinQuantityOrDefault()) {
                    BigDecimal percent = config.getPercentOrDefault();
                    BigDecimal discountAmount = line.lineTotal()
                            .multiply(percent)
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    total = total.add(discountAmount);
                }
            }
        }
        return total;
    }
}

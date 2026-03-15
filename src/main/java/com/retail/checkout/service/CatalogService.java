package com.retail.checkout.service;

import com.retail.checkout.config.CatalogLoader;
import com.retail.checkout.config.PromotionConfig;
import com.retail.checkout.domain.Product;
import com.retail.checkout.dto.ProductCatalogEntryDto;
import com.retail.checkout.dto.PromotionSummaryDto;
import com.retail.checkout.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    private final ProductRepository productRepository;
    private final CatalogLoader catalogLoader;

    public CatalogService(ProductRepository productRepository, CatalogLoader catalogLoader) {
        this.productRepository = productRepository;
        this.catalogLoader = catalogLoader;
    }

    public List<ProductCatalogEntryDto> getProductsWithPromotions() {
        List<Product> products = productRepository.findAll();
        List<ProductCatalogEntryDto> result = new ArrayList<>(products.size());
        for (Product product : products) {
            List<PromotionSummaryDto> promotionDtos = catalogLoader.getPromotionConfigs(product.sku()).stream()
                    .map(config -> configToSummary(product.sku(), config))
                    .collect(Collectors.toList());
            result.add(new ProductCatalogEntryDto(
                    product.sku(),
                    product.name(),
                    product.price(),
                    promotionDtos
            ));
        }
        return result;
    }

    private static PromotionSummaryDto configToSummary(String sku, PromotionConfig config) {
        if (PromotionConfig.TYPE_QUANTITY_DISCOUNT.equals(config.type())) {
            int minQty = config.getMinQuantityOrDefault();
            String percentStr = config.getPercentOrDefault().toString();
            String description = percentStr + "% dcto. por " + minQty + "+ unidades de " + sku;
            return new PromotionSummaryDto("PROMOTION", description);
        }
        return new PromotionSummaryDto(config.type(), config.type());
    }
}

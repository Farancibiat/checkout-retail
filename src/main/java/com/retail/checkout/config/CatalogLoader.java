package com.retail.checkout.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.checkout.domain.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CatalogLoader {

    private static final String CATALOG_RESOURCE = "catalog.json";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, Product> productsBySku = new ConcurrentHashMap<>();
    private final Map<String, List<PromotionConfig>> promotionConfigsBySku = new ConcurrentHashMap<>();

    @PostConstruct
    void load() {
        ClassPathResource resource = new ClassPathResource(CATALOG_RESOURCE);
        try (InputStream is = resource.getInputStream()) {
            List<ProductEntry> entries = objectMapper.readValue(is, new TypeReference<>() {});
            for (ProductEntry entry : entries) {
                Product product = new Product(
                        entry.sku(),
                        entry.name(),
                        entry.getPriceAsBigDecimal()
                );
                productsBySku.put(entry.sku(), product);
                promotionConfigsBySku.put(entry.sku(), new ArrayList<>(entry.getPromotions()));
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar " + CATALOG_RESOURCE, e);
        }
    }

    public Optional<Product> getProduct(String sku) {
        return Optional.ofNullable(productsBySku.get(sku));
    }

    public List<Product> findAllProducts() {
        return new ArrayList<>(productsBySku.values());
    }

    public List<PromotionConfig> getPromotionConfigs(String sku) {
        List<PromotionConfig> configs = promotionConfigsBySku.get(sku);
        return configs != null ? List.copyOf(configs) : Collections.emptyList();
    }
}

package com.retail.checkout.repository;

import com.retail.checkout.config.CatalogLoader;
import com.retail.checkout.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final CatalogLoader catalogLoader;

    public InMemoryProductRepository(CatalogLoader catalogLoader) {
        this.catalogLoader = catalogLoader;
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return catalogLoader.getProduct(sku);
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(catalogLoader.findAllProducts());
    }
}

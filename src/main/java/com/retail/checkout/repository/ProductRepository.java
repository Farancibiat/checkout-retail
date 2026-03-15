package com.retail.checkout.repository;

import com.retail.checkout.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findBySku(String sku);

    List<Product> findAll();
}

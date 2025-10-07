package com.company.scm.repository;

import com.company.scm.model.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory repository for managing Product entities.
 */
public class ProductRepository {
    private final Map<String, Product> products = new HashMap<>();

    public void save(Product product) {
        products.put(product.getProductId(), product);
    }

    public Optional<Product> findById(String productId) {
        return Optional.ofNullable(products.get(productId));
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public void delete(String productId) {
        products.remove(productId);
    }
}
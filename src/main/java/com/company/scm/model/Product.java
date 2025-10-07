package com.company.scm.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a product with its details and pricing.
 */
public class Product {
    private String productId;
    private String name;
    private String description;
    private String category;
    private BigDecimal unitPrice;

    public Product(String productId, String name, String description, String category, BigDecimal unitPrice) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.unitPrice = unitPrice;
    }

    // Getters and Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    @Override
    public String toString() {
        return "Product{" +
               "productId='" + productId + '\'' +
               ", name='" + name + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
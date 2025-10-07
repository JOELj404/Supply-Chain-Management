package com.company.scm.model;

import java.time.LocalDateTime;

/**
 * Represents a specific quantity of a product in a specific warehouse. 
 */
public class InventoryItem {
    private String itemId;
    private String productId;
    private String warehouseId;
    private int quantity;
    private LocalDateTime lastRestockedDate;

    public InventoryItem(String productId, String warehouseId, int quantity) {
        this.itemId = generateItemId(productId, warehouseId);
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
        this.lastRestockedDate = LocalDateTime.now();
    }

    public static String generateItemId(String productId, String warehouseId) {
        return productId + "_" + warehouseId;
    }

    // Getters and Setters
    public String getItemId() { return itemId; }
    public String getProductId() { return productId; }
    public String getWarehouseId() { return warehouseId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public LocalDateTime getLastRestockedDate() { return lastRestockedDate; }
    public void setLastRestockedDate(LocalDateTime lastRestockedDate) { this.lastRestockedDate = lastRestockedDate; }
}
package com.company.scm.model;

import java.time.LocalDateTime;

/**
 * Represents an order to purchase goods from a supplier. 
 */
public class PurchaseOrder {
    public enum Status { PENDING, DELIVERED, CANCELLED }

    private String orderId;
    private String supplierId;
    private String productId;
    private int quantity;
    private LocalDateTime orderDate;
    private Status status;

    public PurchaseOrder(String orderId, String supplierId, String productId, int quantity) {
        this.orderId = orderId;
        this.supplierId = supplierId;
        this.productId = productId;
        this.quantity = quantity;
        this.orderDate = LocalDateTime.now();
        this.status = Status.PENDING; // Default status
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public String getSupplierId() { return supplierId; }
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
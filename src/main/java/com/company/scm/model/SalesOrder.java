package com.company.scm.model;

import java.time.LocalDateTime;

/**
 * Represents a sales order from a customer for a product. 
 */
public class SalesOrder {
    public enum Status { PROCESSING, FULFILLED, CANCELLED }

    private String orderId;
    private String productId;
    private int quantity;
    private LocalDateTime orderDate;
    private Status status;
    private String customerDetails;

    public SalesOrder(String orderId, String productId, int quantity, String customerDetails) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.customerDetails = customerDetails;
        this.orderDate = LocalDateTime.now();
        this.status = Status.PROCESSING; // Default status
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public String getCustomerDetails() { return customerDetails; }
    public void setCustomerDetails(String customerDetails) { this.customerDetails = customerDetails; }
}
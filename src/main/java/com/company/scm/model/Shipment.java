package com.company.scm.model;

import java.time.LocalDateTime;

/**
 * Represents the shipment of an order to a destination. 
 */
public class Shipment {
    public enum Status { IN_TRANSIT, DELIVERED }

    private String shipmentId;
    private String orderId; // Corresponds to a SalesOrder
    private String originWarehouseId;
    private String destination;
    private LocalDateTime shipmentDate;
    private Status status;

    public Shipment(String shipmentId, String orderId, String originWarehouseId, String destination) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.originWarehouseId = originWarehouseId;
        this.destination = destination;
        this.shipmentDate = LocalDateTime.now();
        this.status = Status.IN_TRANSIT; // Default status
    }

    // Getters and Setters
    public String getShipmentId() { return shipmentId; }
    public String getOrderId() { return orderId; }
    public String getOriginWarehouseId() { return originWarehouseId; }
    public String getDestination() { return destination; }
    public LocalDateTime getShipmentDate() { return shipmentDate; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
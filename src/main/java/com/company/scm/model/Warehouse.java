package com.company.scm.model;

import java.util.Objects;

/**
 * Represents a physical warehouse for storing inventory. 
 */
public class Warehouse {
    private String warehouseId;
    private String location;
    private int capacity;

    public Warehouse(String warehouseId, String location, int capacity) {
        this.warehouseId = warehouseId;
        this.location = location;
        this.capacity = capacity;
    }

    // Getters and Setters
    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return Objects.equals(warehouseId, warehouse.warehouseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(warehouseId);
    }
}
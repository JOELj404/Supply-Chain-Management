package com.company.scm.model;

import java.util.Objects;

/**
 * Represents a supplier providing products. 
 */
public class Supplier {
    private String supplierId;
    private String name;
    private String contactInfo;
    private int rating; // e.g., 1-5 rating

    public Supplier(String supplierId, String name, String contactInfo, int rating) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.rating = rating;
    }

    // Getters and Setters
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supplier supplier = (Supplier) o;
        return Objects.equals(supplierId, supplier.supplierId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supplierId);
    }
}
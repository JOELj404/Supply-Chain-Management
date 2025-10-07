package com.company.scm.service;

import com.company.scm.exception.EntityNotFoundException;
import com.company.scm.exception.InsufficientStockException;
import com.company.scm.exception.InvalidQuantityException;
import com.company.scm.model.InventoryItem;
import com.company.scm.repository.InventoryRepository;

/**
 * Manages inventory operations across warehouses.
 */
public class InventoryService {
    
    // Repository for inventory data access
    private final InventoryRepository inventoryRepository;

    /**
     * Creates InventoryService with given repository.
     */
    public InventoryService(InventoryRepository inventoryRepository) {
        if (inventoryRepository == null) {
            throw new IllegalArgumentException("Inventory repository cannot be null");
        }
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Adds stock to a warehouse.
     */
    public void addStockToWarehouse(String productIdentifier, String warehouseIdentifier, int quantityToAdd) 
            throws InvalidQuantityException {
        
        // Input validation
        validateProductAndWarehouseIdentifiers(productIdentifier, warehouseIdentifier);
        validatePositiveQuantity(quantityToAdd, "Quantity to add must be positive");

        // Retrieve existing inventory item or create new one with zero initial quantity
        InventoryItem existingInventoryItem = inventoryRepository
                .findByProductIdAndWarehouseId(productIdentifier, warehouseIdentifier)
                .orElse(new InventoryItem(productIdentifier, warehouseIdentifier, 0));

        // Update quantity and persist changes
        int updatedQuantity = existingInventoryItem.getQuantity() + quantityToAdd;
        existingInventoryItem.setQuantity(updatedQuantity);
        inventoryRepository.save(existingInventoryItem);
    }

    /**
     * Removes stock from a warehouse.
     */
    public void removeStockFromWarehouse(String productIdentifier, String warehouseIdentifier, int quantityToRemove)
            throws InsufficientStockException, InvalidQuantityException, EntityNotFoundException {
        
        // Input validation
        validateProductAndWarehouseIdentifiers(productIdentifier, warehouseIdentifier);
        validatePositiveQuantity(quantityToRemove, "Quantity to remove must be positive");

        // Retrieve existing inventory item or throw exception if not found
        InventoryItem existingInventoryItem = inventoryRepository
                .findByProductIdAndWarehouseId(productIdentifier, warehouseIdentifier)
                .orElseThrow(() -> new EntityNotFoundException(
                    String.format("No inventory found for product '%s' in warehouse '%s'", 
                                  productIdentifier, warehouseIdentifier)));

        // Validate sufficient stock availability
        int currentQuantity = existingInventoryItem.getQuantity();
        if (currentQuantity < quantityToRemove) {
            throw new InsufficientStockException(
                String.format("Insufficient stock for product '%s' in warehouse '%s'. Available: %d, Requested: %d", 
                              productIdentifier, warehouseIdentifier, currentQuantity, quantityToRemove));
        }

        // Update quantity and persist changes
        int updatedQuantity = currentQuantity - quantityToRemove;
        existingInventoryItem.setQuantity(updatedQuantity);
        inventoryRepository.save(existingInventoryItem);
    }

    /**
     * Gets current stock level for a product in a warehouse.
     * Returns 0 if no inventory record exists.
     */
    public int getCurrentStockLevel(String productIdentifier, String warehouseIdentifier) {
        
        // Input validation
        validateProductAndWarehouseIdentifiers(productIdentifier, warehouseIdentifier);
        
        // Query repository and return quantity (0 if item not found)
        return inventoryRepository.findByProductIdAndWarehouseId(productIdentifier, warehouseIdentifier)
                .map(InventoryItem::getQuantity)
                .orElse(0);
    }

    /**
     * Transfers stock from one warehouse to another.
     */
    public void transferStockBetweenWarehouses(String productIdentifier, 
                                             String sourceWarehouseIdentifier, 
                                             String destinationWarehouseIdentifier, 
                                             int quantityToTransfer)
            throws InsufficientStockException, InvalidQuantityException, EntityNotFoundException {
        
        // Validate that source and destination warehouses are different
        if (sourceWarehouseIdentifier.equals(destinationWarehouseIdentifier)) {
            throw new IllegalArgumentException(
                "Source and destination warehouses must be different. Provided: " + sourceWarehouseIdentifier);
        }
        
        // Atomic transfer operation: remove from source, then add to destination
        // If removal fails, no changes are made to either warehouse
        removeStockFromWarehouse(productIdentifier, sourceWarehouseIdentifier, quantityToTransfer);
        addStockToWarehouse(productIdentifier, destinationWarehouseIdentifier, quantityToTransfer);
    }

    // Private validation methods

    // Validates product and warehouse identifiers
    private void validateProductAndWarehouseIdentifiers(String productIdentifier, String warehouseIdentifier) {
        if (productIdentifier == null || productIdentifier.trim().isEmpty()) {
            throw new IllegalArgumentException("Product identifier cannot be null or empty");
        }
        if (warehouseIdentifier == null || warehouseIdentifier.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse identifier cannot be null or empty");
        }
    }

    // Validates quantity is positive
    private void validatePositiveQuantity(int quantity, String errorMessage) throws InvalidQuantityException {
        if (quantity <= 0) {
            throw new InvalidQuantityException(errorMessage + ". Provided: " + quantity);
        }
    }

    // Legacy methods for backward compatibility

    /**
     * @deprecated Use {@link #addStockToWarehouse(String, String, int)} instead.
     * This method is maintained for backward compatibility.
     */
    @Deprecated
    public void addStock(String productId, String warehouseId, int quantity) throws InvalidQuantityException {
        addStockToWarehouse(productId, warehouseId, quantity);
    }

    /**
     * @deprecated Use {@link #removeStockFromWarehouse(String, String, int)} instead.
     * This method is maintained for backward compatibility.
     */
    @Deprecated
    public void removeStock(String productId, String warehouseId, int quantity)
            throws InsufficientStockException, InvalidQuantityException, EntityNotFoundException {
        removeStockFromWarehouse(productId, warehouseId, quantity);
    }

    /**
     * @deprecated Use {@link #getCurrentStockLevel(String, String)} instead.
     * This method is maintained for backward compatibility.
     */
    @Deprecated
    public int getStockLevel(String productId, String warehouseId) {
        return getCurrentStockLevel(productId, warehouseId);
    }

    /**
     * @deprecated Use {@link #transferStockBetweenWarehouses(String, String, String, int)} instead.
     * This method is maintained for backward compatibility.
     */
    @Deprecated
    public void transferStock(String productId, String fromWarehouseId, String toWarehouseId, int quantity)
            throws InsufficientStockException, InvalidQuantityException, EntityNotFoundException {
        transferStockBetweenWarehouses(productId, fromWarehouseId, toWarehouseId, quantity);
    }
}
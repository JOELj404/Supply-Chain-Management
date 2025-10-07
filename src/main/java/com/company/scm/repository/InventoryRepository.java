package com.company.scm.repository;

import com.company.scm.model.InventoryItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory repository for managing InventoryItem entities.
 * Uses a composite key from productId and warehouseId.
 */
public class InventoryRepository {
    private final Map<String, InventoryItem> inventory = new HashMap<>();

    public void save(InventoryItem item) {
        inventory.put(item.getItemId(), item);
    }

    public Optional<InventoryItem> findByProductIdAndWarehouseId(String productId, String warehouseId) {
        String compositeKey = InventoryItem.generateItemId(productId, warehouseId);
        return Optional.ofNullable(inventory.get(compositeKey));
    }

    public List<InventoryItem> findAll() {
        return new ArrayList<>(inventory.values());
    }
}
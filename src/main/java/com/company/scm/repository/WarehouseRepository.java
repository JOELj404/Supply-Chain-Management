package com.company.scm.repository;

import com.company.scm.model.Warehouse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory repository for managing Warehouse entities.
 */
public class WarehouseRepository {
    private final Map<String, Warehouse> warehouses = new HashMap<>();

    public void save(Warehouse warehouse) {
        warehouses.put(warehouse.getWarehouseId(), warehouse);
    }

    public Optional<Warehouse> findById(String warehouseId) {
        return Optional.ofNullable(warehouses.get(warehouseId));
    }

    public List<Warehouse> findAll() {
        return new ArrayList<>(warehouses.values());
    }
}
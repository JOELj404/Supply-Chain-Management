package com.company.scm.service;

import com.company.scm.exception.DuplicateEntityException;
import com.company.scm.exception.EntityNotFoundException;
import com.company.scm.model.Warehouse;
import com.company.scm.repository.WarehouseRepository;

/**
 * Manages warehouse information.
 */
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Warehouse addWarehouse(Warehouse warehouse) throws DuplicateEntityException {
        if (warehouseRepository.findById(warehouse.getWarehouseId()).isPresent()) {
            throw new DuplicateEntityException("Warehouse with ID " + warehouse.getWarehouseId() + " already exists.");
        }
        warehouseRepository.save(warehouse);
        return warehouse;
    }

    public Warehouse findWarehouseById(String warehouseId) throws EntityNotFoundException {
        return warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + warehouseId));
    }
}
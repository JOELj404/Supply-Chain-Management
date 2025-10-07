package com.company.scm.repository;

import com.company.scm.model.Supplier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory repository for managing Supplier entities.
 */
public class SupplierRepository {
    private final Map<String, Supplier> suppliers = new HashMap<>();

    public void save(Supplier supplier) {
        suppliers.put(supplier.getSupplierId(), supplier);
    }

    public Optional<Supplier> findById(String supplierId) {
        return Optional.ofNullable(suppliers.get(supplierId));
    }

    public List<Supplier> findAll() {
        return new ArrayList<>(suppliers.values());
    }
}
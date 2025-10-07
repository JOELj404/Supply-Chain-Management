package com.company.scm.service;

import com.company.scm.exception.DuplicateEntityException;
import com.company.scm.exception.EntityNotFoundException;
import com.company.scm.model.Supplier;
import com.company.scm.repository.SupplierRepository;

/**
 * Manages supplier information.
 */
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Supplier addSupplier(Supplier supplier) throws DuplicateEntityException {
        if (supplierRepository.findById(supplier.getSupplierId()).isPresent()) {
            throw new DuplicateEntityException("Supplier with ID " + supplier.getSupplierId() + " already exists.");
        }
        supplierRepository.save(supplier);
        return supplier;
    }

    public Supplier findSupplierById(String supplierId) throws EntityNotFoundException {
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with ID: " + supplierId));
    }
}
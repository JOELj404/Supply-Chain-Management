package com.company.scm.service;

import com.company.scm.model.InventoryItem;
import com.company.scm.model.SalesOrder;
import com.company.scm.repository.InventoryRepository;
import com.company.scm.repository.SalesOrderRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates various business reports based on inventory and sales data.
 */
public class ReportService {
    private final InventoryRepository inventoryRepository;
    private final SalesOrderRepository salesOrderRepository;

    public ReportService(InventoryRepository inventoryRepository, SalesOrderRepository salesOrderRepository) {
        this.inventoryRepository = inventoryRepository;
        this.salesOrderRepository = salesOrderRepository;
    }

    /**
     * Generates a report of all items with stock levels below a given threshold.
     */
    public List<InventoryItem> generateLowStockReport(int threshold) { //
        return inventoryRepository.findAll().stream()
                .filter(item -> item.getQuantity() < threshold)
                .collect(Collectors.toList());
    }

    /**
     * Generates a report of all sales within a given date range.
     */
    public List<SalesOrder> generateSalesReport(LocalDate startDate, LocalDate endDate) { //
        return salesOrderRepository.findAll().stream()
                .filter(order -> !order.getOrderDate().toLocalDate().isBefore(startDate) && !order.getOrderDate().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    /**
     * Generates a full inventory report.
     */
    public List<InventoryItem> generateInventoryReport() { //
        return inventoryRepository.findAll();
    }
}
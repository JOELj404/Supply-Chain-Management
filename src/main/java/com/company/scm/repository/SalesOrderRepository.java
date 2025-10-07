package com.company.scm.repository;

import com.company.scm.model.SalesOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory repository for managing SalesOrder entities.
 */
public class SalesOrderRepository {
    private final Map<String, SalesOrder> salesOrders = new HashMap<>();

    public void save(SalesOrder order) {
        salesOrders.put(order.getOrderId(), order);
    }

    public Optional<SalesOrder> findById(String orderId) {
        return Optional.ofNullable(salesOrders.get(orderId));
    }

    public List<SalesOrder> findAll() {
        return new ArrayList<>(salesOrders.values());
    }
}
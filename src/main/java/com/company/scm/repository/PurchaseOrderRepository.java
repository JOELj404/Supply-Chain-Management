package com.company.scm.repository;

import com.company.scm.model.PurchaseOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory repository for managing PurchaseOrder entities.
 */
public class PurchaseOrderRepository {
    private final Map<String, PurchaseOrder> purchaseOrders = new HashMap<>();

    public void save(PurchaseOrder order) {
        purchaseOrders.put(order.getOrderId(), order);
    }

    public Optional<PurchaseOrder> findById(String orderId) {
        return Optional.ofNullable(purchaseOrders.get(orderId));
    }

    public List<PurchaseOrder> findAll() {
        return new ArrayList<>(purchaseOrders.values());
    }
}
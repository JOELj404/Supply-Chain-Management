package com.company.scm.repository;

import com.company.scm.model.Shipment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory repository for managing Shipment entities.
 */
public class ShipmentRepository {
    private final Map<String, Shipment> shipments = new HashMap<>();

    public void save(Shipment shipment) {
        shipments.put(shipment.getShipmentId(), shipment);
    }

    public Optional<Shipment> findById(String shipmentId) {
        return Optional.ofNullable(shipments.get(shipmentId));
    }

    public List<Shipment> findAll() {
        return new ArrayList<>(shipments.values());
    }
}
package com.company.scm.service;

import com.company.scm.exception.EntityNotFoundException;
import com.company.scm.exception.InsufficientStockException;
import com.company.scm.exception.InvalidQuantityException;
import com.company.scm.model.PurchaseOrder;
import com.company.scm.model.SalesOrder;
import com.company.scm.model.Shipment;
import com.company.scm.repository.PurchaseOrderRepository;
import com.company.scm.repository.SalesOrderRepository;
import com.company.scm.repository.ShipmentRepository;
import com.company.scm.repository.ProductRepository;
import com.company.scm.repository.SupplierRepository;

import java.util.UUID;

/**
 * Manages sales and purchase orders.
 */
public class OrderService {
    
    // Inventory service for stock operations
    private final InventoryService inventoryManagementService;
    
    // Repository for sales orders
    private final SalesOrderRepository salesOrderRepository;
    
    // Repository for purchase orders
    private final PurchaseOrderRepository purchaseOrderRepository;
    
    // Repository for shipments
    private final ShipmentRepository shipmentRepository;
    
    // Repository for products
    private final ProductRepository productRepository;
    
    // Repository for suppliers
    private final SupplierRepository supplierRepository;

    /**
     * Creates OrderService with required dependencies.
     */
    public OrderService(InventoryService inventoryService, 
                       SalesOrderRepository salesOrderRepository,
                       PurchaseOrderRepository purchaseOrderRepository, 
                       ShipmentRepository shipmentRepository,
                       ProductRepository productRepository,
                       SupplierRepository supplierRepository) {
        
        // Validate all dependencies are provided
        if (inventoryService == null) {
            throw new IllegalArgumentException("Inventory service cannot be null");
        }
        if (salesOrderRepository == null) {
            throw new IllegalArgumentException("Sales order repository cannot be null");
        }
        if (purchaseOrderRepository == null) {
            throw new IllegalArgumentException("Purchase order repository cannot be null");
        }
        if (shipmentRepository == null) {
            throw new IllegalArgumentException("Shipment repository cannot be null");
        }
        if (productRepository == null) {
            throw new IllegalArgumentException("Product repository cannot be null");
        }
        if (supplierRepository == null) {
            throw new IllegalArgumentException("Supplier repository cannot be null");
        }
        
        this.inventoryManagementService = inventoryService;
        this.salesOrderRepository = salesOrderRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.shipmentRepository = shipmentRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    /**
     * Creates a sales order after checking inventory availability.
     */
    public SalesOrder createCustomerSalesOrder(String productIdentifier, 
                                             int requestedQuantity, 
                                             String customerInformation, 
                                             String warehouseIdentifier)
            throws InsufficientStockException, InvalidQuantityException {
        
        // Input validation
        validateOrderParameters(productIdentifier, requestedQuantity, customerInformation);
        
        // Check inventory availability before creating order
        int availableStock = inventoryManagementService.getCurrentStockLevel(productIdentifier, warehouseIdentifier);
        if (availableStock < requestedQuantity) {
            throw new InsufficientStockException(
                String.format("Insufficient inventory for product '%s' in warehouse '%s'. Available: %d, Requested: %d",
                              productIdentifier, warehouseIdentifier, availableStock, requestedQuantity));
        }

        // Generate unique order identifier and create sales order
        String uniqueOrderId = generateSalesOrderId();
        SalesOrder newSalesOrder = new SalesOrder(uniqueOrderId, productIdentifier, requestedQuantity, customerInformation);
        
        // Persist the order and return
        salesOrderRepository.save(newSalesOrder);
        return newSalesOrder;
    }

    /**
     * Fulfills a sales order by deducting inventory and creating shipment.
     */
    public Shipment fulfillCustomerSalesOrder(String salesOrderIdentifier, 
                                            String fulfillmentWarehouseId, 
                                            String shipmentDestination)
            throws EntityNotFoundException, InsufficientStockException, InvalidQuantityException {
        
        // Input validation
        validateFulfillmentParameters(salesOrderIdentifier, fulfillmentWarehouseId, shipmentDestination);
        
        // Retrieve and validate sales order
        SalesOrder orderToFulfill = salesOrderRepository.findById(salesOrderIdentifier)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Sales order not found with identifier: " + salesOrderIdentifier));

        // Verify order hasn't already been fulfilled
        if (orderToFulfill.getStatus() == SalesOrder.Status.FULFILLED) {
            throw new IllegalStateException(
                String.format("Sales order '%s' has already been fulfilled and cannot be processed again", 
                              salesOrderIdentifier));
        }

        // Deduct inventory from the fulfillment warehouse
        inventoryManagementService.removeStockFromWarehouse(orderToFulfill.getProductId(), 
                                                           fulfillmentWarehouseId, 
                                                           orderToFulfill.getQuantity());
        
        // Update order status and persist changes
        orderToFulfill.setStatus(SalesOrder.Status.FULFILLED);
        salesOrderRepository.save(orderToFulfill);

        // Create shipment record for tracking
        String uniqueShipmentId = generateShipmentId();
        Shipment orderShipment = new Shipment(uniqueShipmentId, salesOrderIdentifier, 
                                             fulfillmentWarehouseId, shipmentDestination);
        shipmentRepository.save(orderShipment);

        return orderShipment;
    }
    
    /**
     * Creates a purchase order from a supplier.
     */
    public PurchaseOrder createSupplierPurchaseOrder(String productIdentifier, 
                                                   int requestedQuantity, 
                                                   String supplierIdentifier)
            throws EntityNotFoundException, InvalidQuantityException {
        
        // Validate input parameters
        validatePurchaseOrderParameters(productIdentifier, requestedQuantity, supplierIdentifier);
        
        // Verify product exists in system
        if (!productRepository.findById(productIdentifier).isPresent()) {
            throw new EntityNotFoundException(
                String.format("Product with identifier '%s' does not exist in the system", 
                              productIdentifier));
        }
        
        // Verify supplier exists in system
        if (!supplierRepository.findById(supplierIdentifier).isPresent()) {
            throw new EntityNotFoundException(
                String.format("Supplier with identifier '%s' does not exist in the system", 
                              supplierIdentifier));
        }
        
        // Generate unique purchase order identifier
        String uniquePurchaseOrderId = generatePurchaseOrderId();
        
        // Create and persist purchase order
        PurchaseOrder newPurchaseOrder = new PurchaseOrder(uniquePurchaseOrderId, 
                                                          supplierIdentifier, 
                                                          productIdentifier, 
                                                          requestedQuantity);
        purchaseOrderRepository.save(newPurchaseOrder);
        
        return newPurchaseOrder;
    }

    // Legacy methods for backward compatibility

    /**
     * @deprecated Use {@link #createCustomerSalesOrder(String, int, String, String)} instead.
     * This method is maintained for backward compatibility and creates orders without inventory checks.
     */
    @Deprecated
    public SalesOrder createSalesOrder(String productId, int quantity, String customerId)
            throws EntityNotFoundException, InvalidQuantityException {
        
        // Backward compatibility: create order without inventory validation (like original method)
        validateOrderParameters(productId, quantity, customerId);
        
        // Generate unique sales order identifier
        String uniqueSalesOrderId = generateSalesOrderId();
        
        // Create and persist sales order (no inventory check for backward compatibility)
        SalesOrder newSalesOrder = new SalesOrder(uniqueSalesOrderId, productId, quantity, customerId);
        salesOrderRepository.save(newSalesOrder);
        
        return newSalesOrder;
    }

    /**
     * @deprecated Use {@link #fulfillCustomerSalesOrder(String, String, String)} instead.
     * This method is maintained for backward compatibility.
     */
    @Deprecated
    public Shipment fulfillSalesOrder(String orderId, String warehouseId, String destination)
            throws EntityNotFoundException, InsufficientStockException, InvalidQuantityException {
        return fulfillCustomerSalesOrder(orderId, warehouseId, destination);
    }

    /**
     * @deprecated Use {@link #createSupplierPurchaseOrder(String, int, String)} instead.
     * This method is maintained for backward compatibility.
     */
    @Deprecated
    public PurchaseOrder createPurchaseOrder(String supplierId, String productId, int quantity)
            throws InvalidQuantityException {
        try {
            return createSupplierPurchaseOrder(productId, quantity, supplierId);
        } catch (EntityNotFoundException e) {
            // Original method didn't throw EntityNotFoundException, so we convert it
            throw new InvalidQuantityException("Invalid order parameters: " + e.getMessage());
        }
    }

    // Private helper methods

    // Validates sales order parameters
    private void validateOrderParameters(String productId, int quantity, String customerId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product identifier cannot be null or empty");
        }
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer identifier cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                String.format("Order quantity must be positive, but was: %d", quantity));
        }
    }

    // Validates fulfillment parameters
    private void validateFulfillmentParameters(String orderId, String warehouseId, String destination) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order identifier cannot be null or empty");
        }
        if (warehouseId == null || warehouseId.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse identifier cannot be null or empty");
        }
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination address cannot be null or empty");
        }
    }

    // Validates purchase order parameters
    private void validatePurchaseOrderParameters(String productId, int quantity, String supplierId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product identifier cannot be null or empty");
        }
        if (supplierId == null || supplierId.trim().isEmpty()) {
            throw new IllegalArgumentException("Supplier identifier cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                String.format("Purchase quantity must be positive, but was: %d", quantity));
        }
    }

    // Generates unique sales order ID
    private String generateSalesOrderId() {
        return "SO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Generates unique purchase order ID
    private String generatePurchaseOrderId() {
        return "PO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Generates unique shipment ID
    private String generateShipmentId() {
        return "SHP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
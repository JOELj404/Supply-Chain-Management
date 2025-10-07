package com.company.scm.service;

import com.company.scm.exception.EntityNotFoundException;
import com.company.scm.exception.InsufficientStockException;
import com.company.scm.exception.InvalidQuantityException;
import com.company.scm.model.SalesOrder;
import com.company.scm.model.Shipment;
import com.company.scm.repository.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test suite for the OrderService class.
 */
@Test
public class OrderServiceTest {

    private OrderService orderService;
    private InventoryService inventoryService;
    private SalesOrderRepository salesOrderRepository;

    private final String productId = "PROD-202";
    private final String warehouseId = "W-003";

    @BeforeMethod
    public void setup() throws InvalidQuantityException {
        // Setup all required dependencies for the OrderService
        InventoryRepository inventoryRepository = new InventoryRepository();
        salesOrderRepository = new SalesOrderRepository();
        PurchaseOrderRepository purchaseOrderRepository = new PurchaseOrderRepository();
        ShipmentRepository shipmentRepository = new ShipmentRepository();
        inventoryService = new InventoryService(inventoryRepository);
        ProductRepository productRepository = new ProductRepository();
        SupplierRepository supplierRepository = new SupplierRepository();
        orderService = new OrderService(inventoryService, salesOrderRepository, purchaseOrderRepository, shipmentRepository, productRepository, supplierRepository);

        // Add 25 items to the warehouse for testing
        inventoryService.addStockToWarehouse(productId, warehouseId, 25);
    }

    @Test(description = "Verify an order can be created when stock is sufficient")
    public void testCreateSalesOrder_Success() throws InsufficientStockException, InvalidQuantityException, EntityNotFoundException {
        SalesOrder order = orderService.createSalesOrder(productId, 10, "Customer A");
        Assert.assertNotNull(order);
        Assert.assertNotNull(order.getOrderId());
        Assert.assertEquals(order.getStatus(), SalesOrder.Status.PROCESSING);
    }

    @Test(description = "Ensure creating an order with insufficient stock fails",
          expectedExceptions = InsufficientStockException.class)
    public void testCreateSalesOrder_InsufficientStock_ThrowsException() throws InsufficientStockException, InvalidQuantityException {
        // Try to order 30 items when only 25 are in stock using the new method that checks inventory
        orderService.createCustomerSalesOrder(productId, 30, "Customer B", warehouseId);
    }

    @Test(description = "Verify that fulfilling an order reduces stock and creates a shipment")
    public void testFulfillSalesOrder_Success() throws InsufficientStockException, InvalidQuantityException, EntityNotFoundException {
        // Step 1: Create a valid order
        SalesOrder order = orderService.createSalesOrder(productId, 15, "Customer C");
        Assert.assertEquals(inventoryService.getCurrentStockLevel(productId, warehouseId), 25, "Stock should be unchanged before fulfillment.");

        // Step 2: Fulfill the order
        Shipment shipment = orderService.fulfillSalesOrder(order.getOrderId(), warehouseId, "Pune");

        // Step 3: Verify the state of the system
        // a) Check that the stock level was reduced
        Assert.assertEquals(inventoryService.getCurrentStockLevel(productId, warehouseId), 10, "Stock was not reduced after fulfillment.");

        // b) Check that the order status is updated
        SalesOrder fulfilledOrder = salesOrderRepository.findById(order.getOrderId()).orElseThrow();
        Assert.assertEquals(fulfilledOrder.getStatus(), SalesOrder.Status.FULFILLED);

        // c) Check that a shipment was created
        Assert.assertNotNull(shipment);
        Assert.assertEquals(shipment.getOrderId(), order.getOrderId());
    }

    @Test(description = "Ensure fulfilling a non-existent order throws EntityNotFoundException",
          expectedExceptions = EntityNotFoundException.class)
    public void testFulfillSalesOrder_OrderNotFound_ThrowsException() throws EntityNotFoundException, InsufficientStockException, InvalidQuantityException {
        orderService.fulfillSalesOrder("SO-FAKE-ID", warehouseId, "Mumbai");
    }
}
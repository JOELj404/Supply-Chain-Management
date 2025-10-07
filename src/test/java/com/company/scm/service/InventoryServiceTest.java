package com.company.scm.service;

import com.company.scm.exception.EntityNotFoundException;
import com.company.scm.exception.InsufficientStockException;
import com.company.scm.exception.InvalidQuantityException;
import com.company.scm.repository.InventoryRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test suite for the InventoryService class.
 */
@Test // Marks the entire class as a test class
public class InventoryServiceTest {

    private InventoryService inventoryService;
    private final String productId = "PROD-101";
    private final String warehouseId1 = "W-001";
    private final String warehouseId2 = "W-002";

    /**
     * This method runs before each test method. It sets up a fresh instance
     * of the service and repository to ensure tests are isolated.
     */
    @BeforeMethod
    public void setup() throws InvalidQuantityException {
        InventoryRepository inventoryRepository = new InventoryRepository();
        inventoryService = new InventoryService(inventoryRepository);
        // Pre-load the inventory with 100 items for testing
        inventoryService.addStockToWarehouse(productId, warehouseId1, 100);
    }

    @Test(description = "Verify that adding a positive quantity increases stock level")
    public void testAddStockSuccess() throws InvalidQuantityException {
        inventoryService.addStockToWarehouse(productId, warehouseId1, 50);
        Assert.assertEquals(inventoryService.getCurrentStockLevel(productId, warehouseId1), 150);
    }

    @Test(description = "Ensure adding a negative quantity throws InvalidQuantityException",
          expectedExceptions = InvalidQuantityException.class)
    public void testAddStock_NegativeQuantity_ThrowsException() throws InvalidQuantityException {
        inventoryService.addStockToWarehouse(productId, warehouseId1, -10);
    }

    @Test(description = "Verify that removing an available quantity decreases stock level")
    public void testRemoveStockSuccess() throws InsufficientStockException, InvalidQuantityException, EntityNotFoundException {
        inventoryService.removeStockFromWarehouse(productId, warehouseId1, 40);
        Assert.assertEquals(inventoryService.getCurrentStockLevel(productId, warehouseId1), 60);
    }

    @Test(description = "Ensure removing more stock than available throws InsufficientStockException",
          expectedExceptions = InsufficientStockException.class)
    public void testRemoveStock_InsufficientQuantity_ThrowsException() throws InsufficientStockException, InvalidQuantityException, EntityNotFoundException {
        // Trying to remove 200 items when only 100 are available
        inventoryService.removeStockFromWarehouse(productId, warehouseId1, 200);
    }

    @DataProvider(name = "transferData")
    public Object[][] provideTransferData() {
        return new Object[][]{
                // {qtyToTransfer, expectedSourceQty, expectedDestQty}
                {20, 80, 20},  // Transfer 20 items
                {100, 0, 100}, // Transfer all items
                {1, 99, 1}     // Transfer a single item
        };
    }

    @Test(description = "Test successful stock transfer between two warehouses using a DataProvider",
          dataProvider = "transferData")
    public void testTransferStock_Success(int qtyToTransfer, int expectedSourceQty, int expectedDestQty)
            throws InsufficientStockException, InvalidQuantityException, EntityNotFoundException {
        // Execute the transfer
        inventoryService.transferStockBetweenWarehouses(productId, warehouseId1, warehouseId2, qtyToTransfer);

        // Verify the state of both warehouses
        int sourceStock = inventoryService.getCurrentStockLevel(productId, warehouseId1);
        int destStock = inventoryService.getCurrentStockLevel(productId, warehouseId2);

        Assert.assertEquals(sourceStock, expectedSourceQty, "Source warehouse stock is incorrect.");
        Assert.assertEquals(destStock, expectedDestQty, "Destination warehouse stock is incorrect.");
    }
}
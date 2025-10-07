package com.company.scm.service;

import com.company.scm.exception.InvalidQuantityException;
import com.company.scm.model.InventoryItem;
import com.company.scm.repository.InventoryRepository;
import com.company.scm.repository.SalesOrderRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

/**
 * Test suite for the ReportService class.
 */
@Test
public class ReportServiceTest {

    private ReportService reportService;

    /**
     * Use @BeforeClass for setup that is expensive and can be shared
     * across all test methods in this class.
     */
    @BeforeClass
    public void setup() throws InvalidQuantityException {
        InventoryRepository inventoryRepository = new InventoryRepository();
        SalesOrderRepository salesOrderRepository = new SalesOrderRepository();
        reportService = new ReportService(inventoryRepository, salesOrderRepository);

        // Seed inventory data for report testing
        inventoryRepository.save(new InventoryItem("PROD-A", "W-A", 5));   // Below threshold
        inventoryRepository.save(new InventoryItem("PROD-B", "W-A", 15));  // At threshold
        inventoryRepository.save(new InventoryItem("PROD-C", "W-B", 50));  // Above threshold
        inventoryRepository.save(new InventoryItem("PROD-D", "W-C", 10));  // Below threshold
    }

    @Test(description = "Verify the low stock report only includes items below the specified threshold")
    public void testGenerateLowStockReport() {
        int threshold = 15;
        List<InventoryItem> lowStockItems = reportService.generateLowStockReport(threshold);

        // We expect to find PROD-A (5) and PROD-D (10)
        Assert.assertEquals(lowStockItems.size(), 2, "Report should contain exactly 2 items.");

        // Verify that the correct products are in the report
        boolean foundProdA = lowStockItems.stream().anyMatch(item -> item.getProductId().equals("PROD-A"));
        boolean foundProdD = lowStockItems.stream().anyMatch(item -> item.getProductId().equals("PROD-D"));

        Assert.assertTrue(foundProdA, "Product A should be in the low stock report.");
        Assert.assertTrue(foundProdD, "Product D should be in the low stock report.");
    }

    @Test(description = "Verify the low stock report is empty when no items are below the threshold")
    public void testGenerateLowStockReport_NoResults() {
        int threshold = 5;
        List<InventoryItem> lowStockItems = reportService.generateLowStockReport(threshold);
        Assert.assertTrue(lowStockItems.isEmpty(), "Report should be empty when no items are below the threshold.");
    }
}
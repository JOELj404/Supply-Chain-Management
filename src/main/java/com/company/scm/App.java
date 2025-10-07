package com.company.scm;

import com.company.scm.exception.EntityNotFoundException;
import com.company.scm.exception.InsufficientStockException;
import com.company.scm.exception.InvalidQuantityException;
import com.company.scm.model.*;
import com.company.scm.repository.*;
import com.company.scm.service.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * Main application class with a command-line interface (CLI)
 * to interact with the Supply Chain Management System.
 */
public class App {

    public static void main(String[] args) {
        // =================================================================
        // 1. DEPENDENCY SETUP (IN-MEMORY)
        // =================================================================
        // Repositories (Data Layer)
        ProductRepository productRepository = new ProductRepository();
        WarehouseRepository warehouseRepository = new WarehouseRepository();
        InventoryRepository inventoryRepository = new InventoryRepository();
        SalesOrderRepository salesOrderRepository = new SalesOrderRepository();
        PurchaseOrderRepository purchaseOrderRepository = new PurchaseOrderRepository();
        ShipmentRepository shipmentRepository = new ShipmentRepository();

        // Services (Business Logic Layer)
        InventoryService inventoryService = new InventoryService(inventoryRepository);
        SupplierRepository supplierRepository = new SupplierRepository();
        OrderService orderService = new OrderService(inventoryService, salesOrderRepository, purchaseOrderRepository, shipmentRepository, productRepository, supplierRepository);
        ReportService reportService = new ReportService(inventoryRepository, salesOrderRepository);

        // =================================================================
        // 2. DATA SEEDING (FOR DEMONSTRATION)
        // =================================================================
        try {
            System.out.println("Initializing system with sample data...");
            // Warehouses
            warehouseRepository.save(new Warehouse("W001", "Pune, Maharashtra", 10000));
            warehouseRepository.save(new Warehouse("W002", "Mumbai, Maharashtra", 15000));

            // Products
            productRepository.save(new Product("PROD-001", "Laptop Pro 15", "High-end laptop", "Electronics", new BigDecimal("1200.00")));
            productRepository.save(new Product("PROD-002", "Wireless Mouse", "Ergonomic wireless mouse", "Accessories", new BigDecimal("25.00")));
            productRepository.save(new Product("PROD-003", "Mechanical Keyboard", "RGB Keyboard", "Accessories", new BigDecimal("75.00")));

            // Initial Stock
            inventoryService.addStockToWarehouse("PROD-001", "W001", 50); // 50 Laptops in Pune
            inventoryService.addStockToWarehouse("PROD-002", "W001", 200); // 200 Mice in Pune
            inventoryService.addStockToWarehouse("PROD-003", "W002", 10); // 10 Keyboards in Mumbai (low stock example)
            System.out.println("Initialization complete.");
        } catch (Exception e) {
            System.err.println("Critical error during data initialization: " + e.getMessage());
            return;
        }

        // =================================================================
        // 3. CLI APPLICATION LOOP
        // =================================================================
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1":
                        checkStock(scanner, inventoryService);
                        break;
                    case "2":
                        createSalesOrder(scanner, orderService);
                        break;
                    case "3":
                        fulfillSalesOrder(scanner, orderService);
                        break;
                    case "4":
                        transferStock(scanner, inventoryService);
                        break;
                    case "5":
                        runLowStockReport(scanner, reportService);
                        break;
                    case "6":
                        System.out.println("Exiting application. Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InsufficientStockException | InvalidQuantityException | EntityNotFoundException | IllegalArgumentException | IllegalStateException e) {
                // Catch specific, expected exceptions and show a friendly message
                System.err.println("Operation Failed: " + e.getMessage());
            } catch (Exception e) {
                // Catch any other unexpected exceptions
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n===== Supply Chain Management System Menu =====");
        System.out.println("1. Check Stock Level");
        System.out.println("2. Create Sales Order");
        System.out.println("3. Fulfill Sales Order");
        System.out.println("4. Transfer Stock Between Warehouses");
        System.out.println("5. Run Low Stock Report");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    private static void checkStock(Scanner scanner, InventoryService inventoryService) {
        System.out.print("Enter Product ID (e.g., PROD-001): ");
        String productId = scanner.nextLine();
        System.out.print("Enter Warehouse ID (e.g., W001): ");
        String warehouseId = scanner.nextLine();
        int level = inventoryService.getCurrentStockLevel(productId, warehouseId);
        System.out.println("SUCCESS: Stock level for " + productId + " in " + warehouseId + " is: " + level);
    }

    private static void createSalesOrder(Scanner scanner, OrderService orderService) throws InsufficientStockException, InvalidQuantityException, EntityNotFoundException {
        System.out.print("Enter Product ID: ");
        String productId = scanner.nextLine();
        System.out.print("Enter Quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Customer Details: ");
        String customer = scanner.nextLine();
        System.out.print("Enter Warehouse ID to sell from (e.g., W001): ");
        String warehouseId = scanner.nextLine();

        SalesOrder order = orderService.createSalesOrder(productId, quantity, customer);
        System.out.println("SUCCESS: Sales Order created! Order ID: " + order.getOrderId());
    }

    private static void fulfillSalesOrder(Scanner scanner, OrderService orderService) throws EntityNotFoundException, InsufficientStockException, InvalidQuantityException {
        System.out.print("Enter Sales Order ID to Fulfill: ");
        String orderId = scanner.nextLine();
        System.out.print("Enter Warehouse ID to fulfill from (e.g., W001): ");
        String warehouseId = scanner.nextLine();
        System.out.print("Enter Shipping Destination: ");
        String destination = scanner.nextLine();

        Shipment shipment = orderService.fulfillSalesOrder(orderId, warehouseId, destination);
        System.out.println("SUCCESS: Order " + orderId + " fulfilled. Shipment created with ID: " + shipment.getShipmentId());
    }

    private static void transferStock(Scanner scanner, InventoryService inventoryService) throws InsufficientStockException, InvalidQuantityException, EntityNotFoundException {
        System.out.print("Enter Product ID to transfer: ");
        String productId = scanner.nextLine();
        System.out.print("Enter Source Warehouse ID (e.g., W001): ");
        String fromWarehouse = scanner.nextLine();
        System.out.print("Enter Destination Warehouse ID (e.g., W002): ");
        String toWarehouse = scanner.nextLine();
        System.out.print("Enter Quantity to transfer: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        inventoryService.transferStockBetweenWarehouses(productId, fromWarehouse, toWarehouse, quantity);
        System.out.println("SUCCESS: Transferred " + quantity + " units of " + productId + " from " + fromWarehouse + " to " + toWarehouse + ".");
        System.out.println("New stock in " + fromWarehouse + ": " + inventoryService.getCurrentStockLevel(productId, fromWarehouse));
        System.out.println("New stock in " + toWarehouse + ": " + inventoryService.getCurrentStockLevel(productId, toWarehouse));
    }
    
    private static void runLowStockReport(Scanner scanner, ReportService reportService) {
        System.out.print("Enter stock threshold (e.g., 20): ");
        int threshold = Integer.parseInt(scanner.nextLine());
        List<InventoryItem> lowStockItems = reportService.generateLowStockReport(threshold);

        System.out.println("--- Low Stock Report (Threshold: " + threshold + ") ---");
        if (lowStockItems.isEmpty()) {
            System.out.println("No items are below the stock threshold.");
        } else {
            for (InventoryItem item : lowStockItems) {
                System.out.printf("Product ID: %s, Warehouse ID: %s, Quantity: %d%n",
                        item.getProductId(), item.getWarehouseId(), item.getQuantity());
            }
        }
        System.out.println("------------------------------------");
    }
}
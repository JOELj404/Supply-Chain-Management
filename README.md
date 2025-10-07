# Supply Chain Management System (SCMS) - CLI Backend

A comprehensive backend system to manage the flow of goods from suppliers to customers, handling inventory, purchase orders, sales orders, shipments, and tracking stock levels across warehouses.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Build Instructions](#build-instructions)
- [Test Instructions](#test-instructions)
- [Running the Application](#running-the-application)
- [Usage Examples](#usage-examples)
- [Technical Architecture](#technical-architecture)
- [Testing Strategy](#testing-strategy)
- [Contributing](#contributing)

## Overview

The Supply Chain Management System is a Java-based CLI application built with Maven that provides core functionality for managing:

- **Inventory Management**: Add, remove, and transfer stock between warehouses
- **Order Processing**: Create and fulfill sales and purchase orders
- **Reporting**: Generate inventory, sales, and low-stock reports
- **Exception Handling**: Robust error handling for business rule violations

## Features

### Core Operations
- Add and remove stock from warehouses
- Transfer stock between warehouses
- Create sales orders with stock validation
- Fulfill sales orders with automatic shipment creation
- Create purchase orders for supplier restocking
- Generate comprehensive reports

### Business Rules
- Automatic stock validation before order creation
- Inventory deduction upon order fulfillment
- Shipment tracking for fulfilled orders
- Low-stock threshold reporting

## Project Structure

```
supply-chain-management/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/company/scm/
│   │   │       ├── model/              # Domain entities
│   │   │       │   ├── Product.java
│   │   │       │   ├── Supplier.java
│   │   │       │   ├── Warehouse.java
│   │   │       │   ├── InventoryItem.java
│   │   │       │   ├── PurchaseOrder.java
│   │   │       │   ├── SalesOrder.java
│   │   │       │   └── Shipment.java
│   │   │       ├── exception/          # Custom exceptions
│   │   │       │   ├── InsufficientStockException.java
│   │   │       │   ├── DuplicateEntityException.java
│   │   │       │   ├── EntityNotFoundException.java
│   │   │       │   └── InvalidQuantityException.java
│   │   │       ├── service/            # Business logic
│   │   │       │   ├── InventoryService.java
│   │   │       │   ├── OrderService.java
│   │   │       │   ├── SupplierService.java
│   │   │       │   ├── WarehouseService.java
│   │   │       │   └── ReportService.java
│   │   │       ├── repository/         # Data access layer
│   │   │       │   ├── ProductRepository.java
│   │   │       │   ├── InventoryRepository.java
│   │   │       │   ├── SalesOrderRepository.java
│   │   │       │   ├── PurchaseOrderRepository.java
│   │   │       │   ├── ShipmentRepository.java
│   │   │       │   ├── SupplierRepository.java
│   │   │       │   └── WarehouseRepository.java
│   │   │       └── App.java            # Main CLI application
│   │   └── resources/
│   └── test/
│       └── java/
│           └── com/company/scm/
│               └── service/            # Test suites
│                   ├── InventoryServiceTest.java
│                   ├── OrderServiceTest.java
│                   └── ReportServiceTest.java
├── pom.xml                            # Maven configuration
└── README.md                          # This file
```

## Prerequisites

- **Java Development Kit (JDK)**: Version 11 or higher
- **Apache Maven**: Version 3.6.0 or higher
- **Command Line Interface**: Terminal, Command Prompt, or PowerShell

### Verify Installation

Check if Java and Maven are properly installed:

```bash
# Check Java version
java -version

# Check Maven version
mvn -version
```

## Build Instructions

### 1. Clone or Download the Project

```bash
# If using Git
git clone <repository-url>
cd supply-chain-management

# Or extract from ZIP and navigate to the directory
```

### 2. Clean and Compile

```bash
# Clean previous builds and compile
mvn clean compile
```

### 3. Package the Application

```bash
# Create JAR file
mvn clean package
```

### 4. Verify Build Success

After a successful build, you should see:
```
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

## Test Instructions

The project includes a comprehensive TestNG test suite covering all core functionality.

### Run All Tests

```bash
# Execute all tests
mvn test
```

### Run Specific Test Classes

```bash
# Run inventory service tests only
mvn test -Dtest=InventoryServiceTest

# Run order service tests only
mvn test -Dtest=OrderServiceTest

# Run report service tests only
mvn test -Dtest=ReportServiceTest
```

### Test Reports

After running tests, detailed reports are generated in:
- `target/surefire-reports/index.html` - HTML test report
- `target/surefire-reports/` - XML and text reports

### Expected Test Results

The test suite includes **13 tests** covering:
- Inventory operations (add, remove, transfer stock)
- Exception handling (insufficient stock, invalid quantities)
- Order lifecycle (creation, fulfillment)
- Report generation (low stock, inventory reports)
- Data validation and business rules

All tests should pass with **0 failures** and **0 errors**.

## Running the Application

### Method 1: Using Maven

```bash
# Run the main application
mvn exec:java -Dexec.mainClass="com.company.scm.App"
```

### Method 2: Using JAR file

```bash
# First, package the application
mvn clean package

# Run the generated JAR
java -cp target/supply-chain-management-1.0-SNAPSHOT.jar com.company.scm.App
```

### Method 3: IDE Integration

Import the project into your IDE (IntelliJ IDEA, Eclipse, VS Code) and run the `App.java` main method directly.

## Usage Examples

### CLI Menu Options

When you run the application, you'll see:

```
=== Supply Chain Management System ===
1. Check Stock Level
2. Create Sales Order
3. Fulfill Sales Order
4. Transfer Stock
5. Generate Low Stock Report
6. Exit
Choose an option (1-6):
```

### Example Operations

#### 1. Check Stock Level
```
Enter Product ID (e.g., PROD-001): PROD-001
Enter Warehouse ID (e.g., W001): W001
SUCCESS: Stock level for PROD-001 in W001 is: 50
```

#### 2. Create Sales Order
```
Enter Product ID: PROD-001
Enter Quantity: 10
Enter Customer Details: John Doe - john@example.com
Enter Warehouse ID to sell from: W001
SUCCESS: Sales Order created! Order ID: SO-A1B2C3D4
```

#### 3. Transfer Stock
```
Enter Product ID to transfer: PROD-001
Enter Source Warehouse ID: W001
Enter Destination Warehouse ID: W002
Enter Quantity to transfer: 25
SUCCESS: Transferred 25 units of PROD-001 from W001 to W002.
```

## Technical Architecture

### Design Patterns Used

- **Repository Pattern**: Separates data access logic from business logic
- **Service Layer Pattern**: Encapsulates business logic and rules
- **Dependency Injection**: Manual DI for loose coupling between components

### Key Technologies

- **Java 11**: Core programming language
- **Maven**: Build automation and dependency management
- **TestNG**: Testing framework with advanced features
- **BigDecimal**: Financial precision for pricing
- **LocalDateTime**: Modern date/time handling
- **HashMap**: In-memory data storage

### Exception Handling Strategy

Custom exceptions for specific business scenarios:
- `InsufficientStockException`: When stock is below required quantity
- `InvalidQuantityException`: For negative or zero quantities
- `EntityNotFoundException`: When referencing non-existent entities
- `DuplicateEntityException`: For duplicate entity creation attempts

## Testing Strategy

### Test Coverage Areas

1. **Unit Tests**: Individual service method testing
2. **Integration Tests**: Service interaction testing
3. **Exception Tests**: Error condition validation
4. **State-Based Tests**: Post-operation data verification
5. **Data-Driven Tests**: Multiple input scenarios using DataProviders

### TestNG Features Utilized

- `@BeforeMethod` / `@BeforeClass`: Test setup and data initialization
- `@Test(expectedExceptions)`: Exception testing
- `@DataProvider`: Parameterized testing
- `@Test(description)`: Clear test documentation
- State verification with assertions


---

## Support

For issues or questions regarding the Supply Chain Management System:

1. Check the test reports for detailed error information
2. Verify all prerequisites are properly installed
3. Ensure all tests pass before reporting issues
4. Review the exception handling documentation

## License

This project is developed for educational purposes as part of a Java programming course.

---

**Built with Java, Maven, and TestNG**
package com.company.scm.repository;

import com.company.scm.model.Product;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Test suite for the ProductRepository class.
 * Tests basic CRUD operations and data integrity.
 */
@Test
public class ProductRepositoryTest {

    private ProductRepository productRepository;
    private Product testProduct1;
    private Product testProduct2;

    @BeforeMethod
    public void setup() {
        productRepository = new ProductRepository();
        testProduct1 = new Product("PROD-TEST-001", "Test Laptop", "Gaming laptop for testing", "Electronics", new BigDecimal("999.99"));
        testProduct2 = new Product("PROD-TEST-002", "Test Mouse", "Wireless mouse for testing", "Accessories", new BigDecimal("29.99"));
    }

    @Test(description = "Verify that a product can be saved and retrieved by ID")
    public void testSaveAndFindById() {
        // Save the product
        productRepository.save(testProduct1);

        // Retrieve and verify
        Optional<Product> retrieved = productRepository.findById("PROD-TEST-001");
        Assert.assertTrue(retrieved.isPresent(), "Product should be found after saving");
        Assert.assertEquals(retrieved.get().getProductId(), testProduct1.getProductId());
        Assert.assertEquals(retrieved.get().getName(), testProduct1.getName());
        Assert.assertEquals(retrieved.get().getUnitPrice(), testProduct1.getUnitPrice());
    }

    @Test(description = "Verify that findById returns empty Optional for non-existent product")
    public void testFindByIdNotFound() {
        Optional<Product> result = productRepository.findById("NON-EXISTENT-ID");
        Assert.assertFalse(result.isPresent(), "Should return empty Optional for non-existent product");
    }

    @Test(description = "Verify that multiple products can be saved and retrieved")
    public void testSaveMultipleProducts() {
        // Save multiple products
        productRepository.save(testProduct1);
        productRepository.save(testProduct2);

        // Verify both can be retrieved
        Optional<Product> product1 = productRepository.findById("PROD-TEST-001");
        Optional<Product> product2 = productRepository.findById("PROD-TEST-002");

        Assert.assertTrue(product1.isPresent(), "First product should be found");
        Assert.assertTrue(product2.isPresent(), "Second product should be found");
        Assert.assertNotEquals(product1.get().getProductId(), product2.get().getProductId(), "Products should have different IDs");
    }

    @Test(description = "Verify that findAll returns all saved products")
    public void testFindAll() {
        // Initially should be empty
        List<Product> emptyList = productRepository.findAll();
        Assert.assertTrue(emptyList.isEmpty(), "Repository should be empty initially");

        // Save products
        productRepository.save(testProduct1);
        productRepository.save(testProduct2);

        // Verify findAll returns both products
        List<Product> allProducts = productRepository.findAll();
        Assert.assertEquals(allProducts.size(), 2, "Should return exactly 2 products");
        
        // Verify the products are in the list
        boolean foundProduct1 = allProducts.stream().anyMatch(p -> p.getProductId().equals("PROD-TEST-001"));
        boolean foundProduct2 = allProducts.stream().anyMatch(p -> p.getProductId().equals("PROD-TEST-002"));
        
        Assert.assertTrue(foundProduct1, "First product should be in the list");
        Assert.assertTrue(foundProduct2, "Second product should be in the list");
    }

    @Test(description = "Verify that saving a product with the same ID updates the existing product")
    public void testUpdateProduct() {
        // Save initial product
        productRepository.save(testProduct1);

        // Create updated version with same ID but different details
        Product updatedProduct = new Product("PROD-TEST-001", "Updated Laptop", "Updated gaming laptop", "Electronics", new BigDecimal("1199.99"));
        productRepository.save(updatedProduct);

        // Verify the product was updated
        Optional<Product> retrieved = productRepository.findById("PROD-TEST-001");
        Assert.assertTrue(retrieved.isPresent(), "Product should still exist after update");
        Assert.assertEquals(retrieved.get().getName(), "Updated Laptop", "Product name should be updated");
        Assert.assertEquals(retrieved.get().getUnitPrice(), new BigDecimal("1199.99"), "Product price should be updated");

        // Verify only one product exists (no duplicates)
        List<Product> allProducts = productRepository.findAll();
        Assert.assertEquals(allProducts.size(), 1, "Should still have only one product after update");
    }

    @Test(description = "Verify that delete removes a product from the repository")
    public void testDeleteProduct() {
        // Save product
        productRepository.save(testProduct1);
        productRepository.save(testProduct2);

        // Verify both exist
        Assert.assertEquals(productRepository.findAll().size(), 2, "Should have 2 products before deletion");

        // Delete one product
        productRepository.delete("PROD-TEST-001");

        // Verify deletion
        Optional<Product> deletedProduct = productRepository.findById("PROD-TEST-001");
        Optional<Product> remainingProduct = productRepository.findById("PROD-TEST-002");

        Assert.assertFalse(deletedProduct.isPresent(), "Deleted product should not be found");
        Assert.assertTrue(remainingProduct.isPresent(), "Other product should still exist");
        Assert.assertEquals(productRepository.findAll().size(), 1, "Should have 1 product after deletion");
    }

    @Test(description = "Verify that deleting non-existent product doesn't cause errors")
    public void testDeleteNonExistentProduct() {
        // Save one product
        productRepository.save(testProduct1);

        // Delete non-existent product (should not throw exception)
        productRepository.delete("NON-EXISTENT-ID");

        // Verify original product still exists
        Optional<Product> product = productRepository.findById("PROD-TEST-001");
        Assert.assertTrue(product.isPresent(), "Original product should still exist");
        Assert.assertEquals(productRepository.findAll().size(), 1, "Should still have 1 product");
    }

    @Test(description = "Verify product equality and data integrity")
    public void testProductDataIntegrity() {
        // Save product
        productRepository.save(testProduct1);

        // Retrieve and verify all fields
        Optional<Product> retrieved = productRepository.findById("PROD-TEST-001");
        Assert.assertTrue(retrieved.isPresent(), "Product should be found");

        Product product = retrieved.get();
        Assert.assertEquals(product.getProductId(), "PROD-TEST-001");
        Assert.assertEquals(product.getName(), "Test Laptop");
        Assert.assertEquals(product.getDescription(), "Gaming laptop for testing");
        Assert.assertEquals(product.getCategory(), "Electronics");
        Assert.assertEquals(product.getUnitPrice(), new BigDecimal("999.99"));
    }
}
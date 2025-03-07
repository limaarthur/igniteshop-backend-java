package com.ignite.igniteshop.repositories;

import com.ignite.igniteshop.entities.Product;
import com.ignite.igniteshop.services.ProductService;
import com.ignite.igniteshop.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private long existsId;
    private long nonExistentId;
    private long countTotalProducts;

    @BeforeEach
    public void setUp() {
        existsId = 10L;
        nonExistentId = 1000L;
        countTotalProducts = 35L;
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
        Product product = new Product(null,
                "Phone",
                "Good Phone",
                800.0,
                "https://img.com/img.jpg",
                Instant.parse("2020-10-20T03:00:00Z"
                ));
        product.setId(null);

        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct.getId());
    }

    @Test
    void deleteShouldDeleteObjectWhenIdExists() {

        assertTrue(productRepository.existsById(existsId));

        productRepository.deleteById(existsId);

        assertFalse(productRepository.existsById(existsId));
    }

    @Test
    void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(nonExistentId);
        });

        assertEquals("Id not found " + nonExistentId, exception.getMessage());
    }
}
package com.ignite.igniteshop.repositories;

import com.ignite.igniteshop.services.ProductService;
import com.ignite.igniteshop.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private long existsId;
    private long nonExistentId;

    @BeforeEach
    public void setUp() {
        existsId = 10L;
        nonExistentId = 1L;
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
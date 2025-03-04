package com.ignite.igniteshop.repositories;

import com.ignite.igniteshop.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void deleteShouldDeleteObjectWhenIdExists() {

        // ARRANGE
        Long existsId = 1L;

        // ACT
        productRepository.deleteById(existsId);

        // ASSERT
        Optional<Product> result = productRepository.findById(existsId);
        Assertions.assertFalse(result.isPresent());
    }
}
package com.ignite.igniteshop.repositories;

import com.ignite.igniteshop.entities.Product;
import com.ignite.igniteshop.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;

    @BeforeEach
    void setUpBeforeClass() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 35L;
    }

    @Test // Método save deveria persistir com incremento automático quando o Id for nulo
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {

        Product product = Factory.createdProduct();
        product.setId(null);

        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test // Método delete deve deletar objeto quando Id existir
    public void deleteShouldDeleteObjectWhenIdExists() {

        productRepository.deleteById(existingId);

        Optional<Product> result = productRepository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test // Método delete deve lançar uma exceçao quando Id não existir
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            productRepository.deleteById(nonExistingId);
        });
    }

    @Test // Método findById deveria retornar id quando o id existir
    public void findByIdShouldReturnNonEmptyOptionalWhenIdExist() {

        Optional<Product> result = productRepository.findById(existingId);

        Assertions.assertTrue(result.isPresent());
    }

    @Test // Método não deveria retornar id quando a id não existe
    public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {

        Optional<Product> result = productRepository.findById(nonExistingId);

        Assertions.assertTrue(result.isEmpty());
    }
}
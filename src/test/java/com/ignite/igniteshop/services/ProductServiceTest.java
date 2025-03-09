package com.ignite.igniteshop.services;

import com.ignite.igniteshop.dtos.ProductDTO;
import com.ignite.igniteshop.entities.Product;
import com.ignite.igniteshop.repositories.ProductRepository;
import com.ignite.igniteshop.services.exceptions.DataBaseException;
import com.ignite.igniteshop.services.exceptions.ResourceNotFoundException;
import com.ignite.igniteshop.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private Product product;
    private PageImpl<Product> page;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 3L;
        product = Factory.createdProduct();
        page = new PageImpl<>(List.of(product));

        Mockito.lenient().when(productRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);

        Mockito.lenient().when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.lenient().when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.lenient().when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.lenient().doNothing().when(productRepository).deleteById(existingId);
        Mockito.lenient().doThrow(ResourceNotFoundException.class).when(productRepository).deleteById(nonExistingId);
        Mockito.lenient().doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenExisting() {

        ProductDTO result = productService.findById(existingId);

        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {

            productService.findById(nonExistingId);
        });
    }

    @Test
    public void findAllPagedShouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductDTO> result = productService.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(productRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void deleteShoulThrowResourceNotFoundExceptionWhenIdExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(nonExistingId);
        });

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        Assertions.assertDoesNotThrow(() -> {
            productService.delete(existingId);
        });

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShoulDatabaseExceptionWhenIdExists() {

        Assertions.assertThrows(DataBaseException.class, () -> {
            productService.delete(dependentId);
        });

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(dependentId);
    }
}
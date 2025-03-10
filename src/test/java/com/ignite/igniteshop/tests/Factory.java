package com.ignite.igniteshop.tests;

import com.ignite.igniteshop.dtos.ProductDTO;
import com.ignite.igniteshop.entities.Category;
import com.ignite.igniteshop.entities.Product;

import java.time.Instant;
import java.time.LocalDateTime;

public class Factory {

    public static Product createdProduct() {
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
        product.getCategories().add(createCategory());
        return product;
    }

    public static ProductDTO createdProductDTO() {
        Product product = createdProduct();
        return new ProductDTO(product, product.getCategories());
    }

    public static Category createCategory() {
        return new Category(1L, "Eletronics", LocalDateTime.now());
    }
}

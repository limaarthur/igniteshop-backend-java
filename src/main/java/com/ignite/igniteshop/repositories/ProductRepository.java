package com.ignite.igniteshop.repositories;

import com.ignite.igniteshop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

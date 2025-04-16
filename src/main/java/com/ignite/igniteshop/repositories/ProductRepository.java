package com.ignite.igniteshop.repositories;

import com.ignite.igniteshop.entities.Category;
import com.ignite.igniteshop.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT obj FROM Product obj JOIN obj.categories cat WHERE "
            + "(:category IS NULL OR cat = :category)")
    Page<Product> find(@Param("category") Category category, Pageable pageable);
}

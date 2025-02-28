package com.ignite.igniteshop.repositories;

import com.ignite.igniteshop.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

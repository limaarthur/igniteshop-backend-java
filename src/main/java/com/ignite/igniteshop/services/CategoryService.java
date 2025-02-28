package com.ignite.igniteshop.services;

import com.ignite.igniteshop.dtos.CategoryDTO;
import com.ignite.igniteshop.entities.Category;
import com.ignite.igniteshop.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryDTO> categoryDTO = categoryList.stream().map(category -> new CategoryDTO(category)).toList();
        return categoryDTO;
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        Category category = optionalCategory.get();
        return new CategoryDTO(category);
    }
}

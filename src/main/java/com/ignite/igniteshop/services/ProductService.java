package com.ignite.igniteshop.services;

import com.ignite.igniteshop.dtos.CategoryDTO;
import com.ignite.igniteshop.dtos.ProductDTO;
import com.ignite.igniteshop.entities.Category;
import com.ignite.igniteshop.entities.Product;
import com.ignite.igniteshop.repositories.CategoryRepository;
import com.ignite.igniteshop.repositories.ProductRepository;
import com.ignite.igniteshop.services.exceptions.DataBaseException;
import com.ignite.igniteshop.services.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;

    private CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Long categoryId, String name, Pageable pageable) {
        Category category = (categoryId == 0) ? null : categoryRepository.getReferenceById(categoryId);
        Page<Product> productList = productRepository.find(category, name, pageable);
        return productList.map(product -> new ProductDTO(product));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(product, product.getCategories());
    }

    @Transactional()
    public ProductDTO insert(ProductDTO productDTO) {
        Product entity = new Product();
        copyDtoToEntity(productDTO, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found " + id));
        copyDtoToEntity(productDTO, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation " + id);
        }
    }

    private void copyDtoToEntity(ProductDTO productDTO, Product entity) {
        entity.setName(productDTO.getName());
        entity.setDescription(productDTO.getDescription());
        entity.setDate(productDTO.getDate());
        entity.setImgUrl(productDTO.getImgUrl());
        entity.setPrice(productDTO.getPrice());

        entity.getCategories().clear();
        for (CategoryDTO categoryDTO : productDTO.getCategories()) {
            Category category = categoryRepository.getReferenceById(categoryDTO.getId());
            entity.getCategories().add(category);
        }
    }
}

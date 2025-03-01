package com.ignite.igniteshop.services;

import com.ignite.igniteshop.dtos.ProductDTO;
import com.ignite.igniteshop.entities.Product;
import com.ignite.igniteshop.repositories.ProductRepository;
import com.ignite.igniteshop.services.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> productList = productRepository.findAll(pageRequest);
        List<ProductDTO> productDTO = productList.map(product -> new ProductDTO(product)).toList();
        return new PageImpl<>(productDTO, pageRequest, productList.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(product);
    }

    @Transactional()
    public ProductDTO insert(ProductDTO productDTO) {
        Product entity = new Product();
        entity.setName(productDTO.getName());
        entity.setDescription(productDTO.getDescription());
        entity.setPrice(productDTO.getPrice());
        entity.setImgUrl(productDTO.getImgUrl());
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found " + id));
        entity.setName(productDTO.getName());
        entity.setDescription(productDTO.getDescription());
        entity.setPrice(productDTO.getPrice());
        entity.setImgUrl(productDTO.getImgUrl());
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    public void delete(Long id) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found " + id));
        productRepository.delete(entity);
    }
}

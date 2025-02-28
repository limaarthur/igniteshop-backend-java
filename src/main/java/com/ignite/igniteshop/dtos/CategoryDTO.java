package com.ignite.igniteshop.dtos;

import com.ignite.igniteshop.entities.Category;

import java.time.LocalDateTime;

public class CategoryDTO {

    private Long id;

    private String name;

    private LocalDateTime createdAt;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String name, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public CategoryDTO(Category entity) {
        id = entity.getId();
        name = entity.getName();
        createdAt = entity.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

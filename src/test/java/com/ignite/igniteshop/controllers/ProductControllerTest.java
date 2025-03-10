package com.ignite.igniteshop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ignite.igniteshop.dtos.ProductDTO;
import com.ignite.igniteshop.services.ProductService;
import com.ignite.igniteshop.services.exceptions.DataBaseException;
import com.ignite.igniteshop.services.exceptions.ResourceNotFoundException;
import com.ignite.igniteshop.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.accept;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 2L;

        productDTO = Factory.createdProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        Mockito.when(productService.findAllPaged(any())).thenReturn(page);

        Mockito.when(productService.findById(existingId)).thenReturn(productDTO);
        Mockito.when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(productService.insert(ArgumentMatchers.any())).thenReturn(productDTO);

        Mockito.when(productService.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(productDTO);
        Mockito.when(productService.update(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(productService).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(productService).delete(nonExistingId);
        Mockito.doThrow(DataBaseException.class).when(productService).delete(dependentId);
    }

    @Test
    public void insertShouldReturnProductDTOCreated() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldreturnProductDTOWhenIdExists() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldreturnNotFoundWhenIdDoesnotExists() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody).
                contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldreturnProductDTOWhenIdExists() throws Exception{

        ResultActions result = mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShouldreturnNotFOundWhenIdDoesnotExists() throws Exception{

        ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void FindAllShouldReturnPage()throws Exception {

        ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }
}
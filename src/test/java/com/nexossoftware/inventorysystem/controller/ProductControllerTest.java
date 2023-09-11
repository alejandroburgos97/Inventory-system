package com.nexossoftware.inventorysystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.nexossoftware.inventorysystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nexossoftware.inventorysystem.payload.ProductDto;
import com.nexossoftware.inventorysystem.payload.SearchCriteria;
import com.nexossoftware.inventorysystem.repository.ProductRepository;
import com.nexossoftware.inventorysystem.service.ProductService;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
    }

    @Test
    public void givenSearchCriteria_whenGetProducts_thenReturnProductsList() throws Exception {
        // given
        ProductDto productDto = new ProductDto();
        productDto.setName("productA");
        productDto.setQuantity(20);
        productDto.setEntryDate(LocalDate.now());
        productDto.setRegisteredUserId(2L);
        productDto.setModifiedUserId(2L);

        ProductDto createdProduct = productService.createProduct(productDto);

        SearchCriteria searchCriteria = new SearchCriteria("productA", null, null);

        // when
        ResultActions response = mockMvc.perform(get("/inventory/products")
                .param("name", searchCriteria.getName()));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", is(1)));
    }


    @Test
    public void givenProductDto_whenCreateProduct_thenReturnCreatedProduct() throws Exception {
        // given
        ProductDto productDto = new ProductDto();
        productDto.setName("productd");
        productDto.setQuantity(10);
        productDto.setEntryDate(LocalDate.now());
        productDto.setRegisteredUserId(1L);

        // when
        ResultActions response = mockMvc.perform(post("/inventory/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(productDto.getName())))
                .andExpect(jsonPath("$.quantity", is(productDto.getQuantity())))
                .andExpect(jsonPath("$.entryDate", is(productDto.getEntryDate().toString())))
                .andExpect(jsonPath("$.registeredUserId", is(productDto.getRegisteredUserId().intValue())));
    }

    @Test
    public void givenProductDto_whenUpdateProduct_thenReturnUpdatedProduct() throws Exception {
        // given
        ProductDto productDto = new ProductDto();
        productDto.setName("productA");
        productDto.setQuantity(20);
        productDto.setEntryDate(LocalDate.now());
        productDto.setRegisteredUserId(2L);
        productDto.setModifiedUserId(2L);

        ProductDto createdProduct = productService.createProduct(productDto);

        productDto.setId(createdProduct.getId());
        productDto.setName("productB");
        productDto.setQuantity(30);

        // when
        ResultActions response = mockMvc.perform(put("/inventory/products/{productId}", createdProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(productDto.getName())))
                .andExpect(jsonPath("$.quantity", is(productDto.getQuantity())))
                .andExpect(jsonPath("$.entryDate", is(productDto.getEntryDate().toString())))
                .andExpect(jsonPath("$.registeredUserId", is(productDto.getRegisteredUserId().intValue())));
    }

    @Test
    public void givenProductIdAndUserId_whenDeleteProduct_thenNoContent() throws Exception {
        // given
        ProductDto productDto = new ProductDto();
        productDto.setName("productA");
        productDto.setQuantity(10);
        productDto.setEntryDate(LocalDate.now());
        productDto.setRegisteredUserId(1L);

        ProductDto createdProduct = productService.createProduct(productDto);

        long userId = 1L;

        // when
        ResultActions response = mockMvc.perform(delete("/inventory/products/{productId}", createdProduct.getId())
                .param("userId", String.valueOf(userId)));

        // then
        response.andExpect(status().isNoContent());
    }
}
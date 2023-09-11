package com.nexossoftware.inventorysystem.service;

import com.nexossoftware.inventorysystem.payload.ProductDto;
import com.nexossoftware.inventorysystem.payload.SearchCriteria;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ProductService {
    List<ProductDto> getProducts(SearchCriteria searchCriteria);
    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(long productId, ProductDto productDto);
    void deleteProduct(long productId, long userId) throws AccessDeniedException;
    List<ProductDto> getAllProducts();
}

package com.nexossoftware.inventorysystem.controller;

import com.nexossoftware.inventorysystem.payload.ProductDto;
import com.nexossoftware.inventorysystem.payload.SearchCriteria;
import com.nexossoftware.inventorysystem.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getProducts(@RequestParam(required=false) String name,
                                                        @RequestParam(required=false) String userEntered,
                                                        @RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateEntered){
        SearchCriteria searchCriteria = new SearchCriteria(name, userEntered, dateEntered);

        if (searchCriteria.isEmpty()) {
            List<ProductDto> allProducts = productService.getAllProducts();
            return new ResponseEntity<>(allProducts, HttpStatus.OK);
        }

        List<ProductDto> products = productService.getProducts(searchCriteria);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto ProductDto){
        ProductDto createdProduct = productService.createProduct(ProductDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable(value = "productId") long productId,
                                              @Valid @RequestBody ProductDto ProductDto){
        ProductDto updatedProduct = productService.updateProduct(productId, ProductDto);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(value = "productId") long productId, @RequestParam(value = "userId") long userId) throws AccessDeniedException {
        productService.deleteProduct(productId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
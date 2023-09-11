package com.nexossoftware.inventorysystem.service.impl;

import com.nexossoftware.inventorysystem.entity.Product;
import com.nexossoftware.inventorysystem.entity.User;
import com.nexossoftware.inventorysystem.entity.specification.ProductSpecifications;
import com.nexossoftware.inventorysystem.exception.InventoryAPIException;
import com.nexossoftware.inventorysystem.exception.ResourceNotFoundException;
import com.nexossoftware.inventorysystem.payload.ProductDto;
import com.nexossoftware.inventorysystem.payload.SearchCriteria;
import com.nexossoftware.inventorysystem.repository.ProductRepository;
import com.nexossoftware.inventorysystem.repository.UserRepository;
import com.nexossoftware.inventorysystem.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }
    @Override
    public List<ProductDto> getProducts(SearchCriteria searchCriteria) {

        Specification<Product> productSpecification = Specification.where(null);

        if (searchCriteria.getName() != null) {
            productSpecification = productSpecification.and(ProductSpecifications.nameContains(searchCriteria.getName()));
        }
        if (searchCriteria.getUserEntered() != null) {
            User user = userRepository.findByName(searchCriteria.getUserEntered());
            productSpecification = productSpecification.and(ProductSpecifications.userIs(user));
        }
        if (searchCriteria.getDateEntered() != null) {
            productSpecification = productSpecification.and(ProductSpecifications.dateIs(searchCriteria.getDateEntered()));
        }

        List<Product> products = productRepository.findAll(productSpecification);
        return convertToDtoList(products);
    }
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = convertToEntity(productDto);
        Product createdProduct = productRepository.save(product);
        return convertToDto(createdProduct);
    }
    @Override
    public ProductDto updateProduct(long productId, ProductDto productDto) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));

        Product updatedProduct = updateProductFields(existingProduct, productDto);
        Product savedProduct = productRepository.save(updatedProduct);
        return convertToDto(savedProduct);
    }
    @Override
    public void deleteProduct(long productId, long userId) throws AccessDeniedException {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Id", productId));
        Long registeredUserId = existingProduct.getRegisteredUser().getId();
        if (!registeredUserId.equals(userId)) {
            throw new AccessDeniedException("The user does not have permission to delete this product.");
        }
        productRepository.delete(existingProduct);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return convertToDtoList(products);
    }
    private Product convertToEntity(ProductDto productDto) {
        return modelMapper.map(productDto, Product.class);
    }

    private ProductDto convertToDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }

    private List<ProductDto> convertToDtoList(List<Product> products) {
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    private Product updateProductFields(Product existingProduct, ProductDto productDto) {
        existingProduct.setName(productDto.getName());
        existingProduct.setQuantity(productDto.getQuantity());
        existingProduct.setModificationDate(productDto.getModificationDate());
        User user = userRepository.findById(productDto.getModifiedUserId())
                .orElseThrow(() -> new InventoryAPIException(HttpStatus.BAD_REQUEST, "The editing user does not exist"));
        existingProduct.setModifiedUser(user);
        return existingProduct;
    }
}

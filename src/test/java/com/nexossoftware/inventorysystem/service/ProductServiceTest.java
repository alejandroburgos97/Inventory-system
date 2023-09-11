package com.nexossoftware.inventorysystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import com.nexossoftware.inventorysystem.entity.Product;
import com.nexossoftware.inventorysystem.entity.User;
import com.nexossoftware.inventorysystem.exception.ResourceNotFoundException;
import com.nexossoftware.inventorysystem.payload.ProductDto;
import com.nexossoftware.inventorysystem.payload.SearchCriteria;
import com.nexossoftware.inventorysystem.repository.ProductRepository;
import com.nexossoftware.inventorysystem.repository.UserRepository;
import com.nexossoftware.inventorysystem.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository mockProductRepository;
    @Mock
    private ModelMapper mockModelMapper;

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void testCreateProduct() {

        // Given
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setQuantity(10);
        productDto.setEntryDate(LocalDate.now());
        productDto.setRegisteredUserId(1L);
        productDto.setModificationDate(LocalDate.now());
        productDto.setModifiedUserId(2L);

        Product product = new Product();
        Product savedProduct = new Product();

        // When
        when(mockModelMapper.map(productDto, Product.class)).thenReturn(product);
        when(mockProductRepository.save(product)).thenReturn(savedProduct);
        when(mockModelMapper.map(savedProduct, ProductDto.class)).thenReturn(productDto);

        ProductDto createdProduct = productService.createProduct(productDto);

        // Then
        assertNotNull(createdProduct);
        assertEquals(productDto, createdProduct);

        verify(mockModelMapper).map(productDto, Product.class);
        verify(mockProductRepository).save(product);
        verify(mockModelMapper).map(savedProduct, ProductDto.class);
    }

    @Test
    void getProducts_allFieldsNonNull_shouldCallSpecificationsAndReturnDtoList() {
        // Given
        String nameSearch = "test";
        LocalDate localDateSearch = LocalDate.of(2023, 6, 30);
        Date dateSearch = Date.from(localDateSearch.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        String userEntered = "test-user";

        User user = new User();
        user.setName(userEntered);

        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setName(nameSearch);
        searchCriteria.setUserEntered(userEntered);
        searchCriteria.setDateEntered(dateSearch);

        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setName(nameSearch);
        productList.add(product);

        // When
        when(mockUserRepository.findByName(userEntered)).thenReturn(user);
        when(mockProductRepository.findAll(any(Specification.class))).thenReturn(productList);

        List<ProductDto> returnedProductDtoList = productService.getProducts(searchCriteria);

        // Then
        verify(mockProductRepository, times(1)).findAll(any(Specification.class));
        assertNotNull(returnedProductDtoList);
    }

    @Test
    void getProducts_allFieldsNull_shouldCallFindAllAndReturnDtoList() {
        // Given
        SearchCriteria searchCriteria = new SearchCriteria();

        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setName("test");
        productList.add(product);

        // When
        when(mockProductRepository.findAll(any(Specification.class))).thenReturn(productList);

        List<ProductDto> returnedProductDtoList = productService.getProducts(searchCriteria);

        // Then
        verify(mockProductRepository, times(1)).findAll(any(Specification.class));
        assertNotNull(returnedProductDtoList);
    }

    @Test
    public void updateProduct_productExists_shouldReturnUpdatedProduct() {

        // Given
        Long testUserId = 2L;
        User user = new User();
        user.setId(testUserId);

        Long testId = 1L;
        Product product = new Product();
        product.setId(testId);
        product.setName("Product Before Update");
        product.setModifiedUser(user);

        ProductDto productDto = new ProductDto();
        productDto.setId(testId);
        productDto.setName("Product After Update");
        productDto.setRegisteredUserId(2L);
        productDto.setModifiedUserId(2L);
        productDto.setModifiedUserId(testUserId);

        Product updatedProductEntity = new Product();
        updatedProductEntity.setId(testId);
        updatedProductEntity.setName(productDto.getName());

        ProductDto updatedProductDto = new ProductDto();
        updatedProductDto.setId(updatedProductEntity.getId());
        updatedProductDto.setName(updatedProductEntity.getName());
        updatedProductDto.setRegisteredUserId(2L);
        updatedProductDto.setModifiedUserId(2L);

        // When
        when(mockUserRepository.findById(testUserId)).thenReturn(Optional.of(user));
        when(mockProductRepository.findById(testId)).thenReturn(Optional.of(product));
        when(mockProductRepository.save(any(Product.class))).thenReturn(updatedProductEntity);
        when(mockModelMapper.map(updatedProductEntity, ProductDto.class)).thenReturn(updatedProductDto);

        ProductDto actualProductDto = productService.updateProduct(testId, productDto);

        // Then
        assertEquals(updatedProductDto, actualProductDto);
        assertEquals("Product After Update", actualProductDto.getName());
    }

    @Test
    public void updateProduct_productDoesNotExist_shouldThrowException() {
        // Given
        Long thisTestId = 1L;

        ProductDto productDto = new ProductDto();
        productDto.setId(thisTestId);

        // When
        when(mockProductRepository.findById(thisTestId)).thenReturn(Optional.empty());

        // Then
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(thisTestId, productDto);
        });
    }

    @Test
    public void testGetAllProducts() {
        // Given
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(mockProductRepository.findAll()).thenReturn(products);

        // When
        List<ProductDto> productDtos = productService.getAllProducts();

        // Then
        assertEquals(products.size(), productDtos.size());
        verify(mockProductRepository).findAll();
    }

    @Test
    public void deleteProduct_productExistsAndUserIsOwner_productDeleted() throws AccessDeniedException {

        Long productId = 1L;
        long userId = 1L;

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(productId);
        product.setRegisteredUser(user);

        when(mockProductRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.deleteProduct(productId, userId);

        verify(mockProductRepository, times(1)).delete(product);
    }

    @Test
    public void deleteProduct_productExistsAndUserIsNotOwner_throwsAccessDeniedException() {

        Long productId = 1L;
        long userId = 2L;
        Long otherUserId = 3L;

        User otherUser = new User();
        otherUser.setId(otherUserId);

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(productId);
        product.setRegisteredUser(otherUser);

        when(mockProductRepository.findById(productId)).thenReturn(Optional.of(product));

        Exception exception = assertThrows(AccessDeniedException.class, () -> productService.deleteProduct(productId, userId));
        assertTrue(exception.getMessage().contains("The user does not have permission to delete this product."));
    }

    @Test
    public void deleteProduct_productDoesNotExist_throwsResourceNotFoundException() {

        long productId = 1L;
        long userId = 1L;

        when(mockProductRepository.findById(productId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(productId, userId));
        assertTrue(exception.getMessage().contains("Product"));
    }
}
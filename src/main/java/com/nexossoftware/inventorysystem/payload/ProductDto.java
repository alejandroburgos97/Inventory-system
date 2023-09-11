package com.nexossoftware.inventorysystem.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProductDto {
    private Long id;
    @NotBlank(message = "Product name is mandatory")
    private String name;

    @Min(1)
    private int quantity;

    @PastOrPresent(message = "Entry date must be today or earlier")
    private LocalDate entryDate;

    private Long registeredUserId;

    @PastOrPresent(message = "Entry date must be today or earlier")
    private LocalDate modificationDate;

    private Long modifiedUserId;
}
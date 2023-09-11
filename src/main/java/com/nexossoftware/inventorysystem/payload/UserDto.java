package com.nexossoftware.inventorysystem.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;


@Data
public class UserDto {
    private Long id;

    @NotBlank(message = "User name is mandatory")
    private String name;

    @Min(value = 18, message = "User must be at least 18 years old")
    private Integer age;

    private Long positionId;

    @PastOrPresent(message = "Company entry date must be today or earlier")
    private LocalDate companyEntryDate;
}
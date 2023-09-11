package com.nexossoftware.inventorysystem.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PositionDto {
    private Long id;

    @NotBlank(message = "Position name is mandatory")
    private String positionName;
}
package com.assignment.stockapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDTO {
    @NotBlank(message = "name is mandatory.")
    private String name;
    @NotNull(message = "currentPrice is mandatory.")
    private Double currentPrice;
}

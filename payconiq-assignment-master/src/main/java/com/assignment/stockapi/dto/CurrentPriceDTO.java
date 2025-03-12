package com.assignment.stockapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrentPriceDTO {
    @NotNull(message = "currentPrice is mandatory.")
    private Double currentPrice;
}

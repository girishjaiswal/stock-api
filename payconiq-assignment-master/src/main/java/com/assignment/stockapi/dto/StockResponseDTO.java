package com.assignment.stockapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class StockResponseDTO {
    private Long id;
    private String name;
    private Double currentPrice;
    private Timestamp lastUpdate;
}

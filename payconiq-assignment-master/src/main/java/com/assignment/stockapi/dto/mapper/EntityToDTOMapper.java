package com.assignment.stockapi.dto.mapper;

import com.assignment.stockapi.dto.StockResponseDTO;
import com.assignment.stockapi.entity.Stock;
import org.springframework.data.domain.Page;

public class EntityToDTOMapper {

    public static StockResponseDTO transform(Stock stock) {
        return new StockResponseDTO(stock.getId(), stock.getName(), stock.getCurrentPrice(), stock.getLastUpdate());
    }

    public static Page<StockResponseDTO> transform(Page<Stock> stock) {
        return stock.map(stock1 -> new StockResponseDTO(stock1.getId(), stock1.getName(), stock1.getCurrentPrice(), stock1.getLastUpdate()));
    }
}


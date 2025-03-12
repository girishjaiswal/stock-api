package com.assignment.stockapi.controller;

import com.assignment.stockapi.dto.CurrentPriceDTO;
import com.assignment.stockapi.dto.StockDTO;
import com.assignment.stockapi.dto.StockResponseDTO;
import com.assignment.stockapi.dto.mapper.EntityToDTOMapper;
import com.assignment.stockapi.entity.Stock;
import com.assignment.stockapi.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/api")
public class StockController {
    private final StockService stockService;
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }
    @Operation(summary = "this endpoint is used to get all stocks.")
    @GetMapping("/stocks")
    public Page<StockResponseDTO> getStocks(@AuthenticationPrincipal Jwt jwt, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "elementPerPage", required = false) Integer elementPerPage) {
        if (page == null)
            page = 0;
        if (elementPerPage == null)
            elementPerPage = 10;
        return EntityToDTOMapper.transform(stockService.getStocks(page, elementPerPage));
    }
    @GetMapping("/stocks/{stockId}")
    public ResponseEntity<StockResponseDTO> getStockById(@PathVariable("stockId") long stockId) {
        Optional<Stock> stockData = stockService.getStockById(stockId);
        if (stockData.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(EntityToDTOMapper.transform(stockData.get()), HttpStatus.OK);
    }
    @PostMapping("/stocks")
    public ResponseEntity<StockResponseDTO> createStock(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody StockDTO stockDTO) {
        Stock stock = stockService.createStock(new Stock(stockDTO), jwt.getClaim("sub"));
        return new ResponseEntity<>(EntityToDTOMapper.transform(stock), HttpStatus.CREATED);
    }
    @PatchMapping("/stocks/{stockId}")
    public ResponseEntity<StockResponseDTO> updateStock(@AuthenticationPrincipal Jwt jwt, @PathVariable("stockId") long stockId, @Valid @RequestBody CurrentPriceDTO currentPriceDTO) {
        Optional<Stock> stockData = stockService.getStockById(stockId);
        if (stockData.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!jwt.getClaim("AUTHORITY").toString().equalsIgnoreCase("ADMIN") && !jwt.getClaim("sub").toString().equalsIgnoreCase(stockData.get().getUserName()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Stock stock = stockData.get();
        stock.setCurrentPrice(currentPriceDTO.getCurrentPrice());
        stockService.saveStock(stock);
        return new ResponseEntity<>(EntityToDTOMapper.transform(stock), HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/stocks/{stockId}")
    public ResponseEntity<HttpStatus> deleteStock(@AuthenticationPrincipal Jwt jwt, @PathVariable("stockId") long stockId) {
        Optional<Stock> stockData = stockService.getStockById(stockId);
        if (stockData.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!jwt.getClaim("AUTHORITY").toString().equalsIgnoreCase("ADMIN") && !jwt.getClaim("sub").toString().equalsIgnoreCase(stockData.get().getUserName()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        stockService.deleteStock(stockId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

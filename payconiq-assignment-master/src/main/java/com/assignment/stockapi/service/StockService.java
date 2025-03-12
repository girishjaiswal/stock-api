package com.assignment.stockapi.service;

import com.assignment.stockapi.entity.Stock;
import com.assignment.stockapi.repository.StockRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class StockService {
    private final StockRepository stockRepository;
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
    public Page<Stock> getStocks(int page, int elementPerPage) {
        return stockRepository.findAll(PageRequest.of(page, elementPerPage));
    }
    public Optional<Stock> getStockById(long stockId) {
        return stockRepository.findById(stockId);
    }
    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }
    public Stock createStock(Stock stock, String user) {
        stock.setUserName(user);
        return stockRepository.save(stock);
    }
    public void deleteStock(long stockId) {
        stockRepository.deleteById(stockId);
    }
}

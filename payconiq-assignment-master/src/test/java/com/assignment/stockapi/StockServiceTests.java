package com.assignment.stockapi;

import com.assignment.stockapi.entity.Stock;
import com.assignment.stockapi.repository.StockRepository;
import com.assignment.stockapi.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    private Stock stock;

    @BeforeEach
    void setUp() {
        stock = new Stock();
        stock.setId(1L);
        stock.setName("Test Stock");
    }

    @Test
    void testGetStocks() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Stock> stockPage = new PageImpl<>(Arrays.asList(stock));
        when(stockRepository.findAll(pageRequest)).thenReturn(stockPage);

        Page<Stock> result = stockService.getStocks(0, 5);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(stockRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void testGetStockById() {
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

        Optional<Stock> result = stockService.getStockById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Stock", result.get().getName());
        verify(stockRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveStock() {
        when(stockRepository.save(stock)).thenReturn(stock);

        Stock result = stockService.saveStock(stock);

        assertNotNull(result);
        assertEquals("Test Stock", result.getName());
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    void testCreateStock() {
        String user = "testUser";
        when(stockRepository.save(stock)).thenReturn(stock);

        Stock result = stockService.createStock(stock, user);

        assertNotNull(result);
        assertEquals(user, result.getUserName());
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    void testDeleteStock() {
        doNothing().when(stockRepository).deleteById(1L);

        stockService.deleteStock(1L);

        verify(stockRepository, times(1)).deleteById(1L);
    }
}
package com.assignment.stockapi;

import com.assignment.stockapi.controller.StockController;
import com.assignment.stockapi.entity.Stock;
import com.assignment.stockapi.repository.StockRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ContextConfiguration(classes = TestSecurityConfig.class)
@WebMvcTest(value = StockController.class)
@ActiveProfiles("dev")
public class StockControllerTests {

    @MockBean
    private StockRepository stockRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getStockById() throws Exception {
        long id = 1L;
        Stock stock = getDummyStock();
        when(stockRepository.findById(id)).thenReturn(Optional.of(stock));
        mockMvc.perform(get("/api/stocks/{stockId}", id).header(HttpHeaders.AUTHORIZATION, "Bearer token")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.currentPrice").value(stock.getCurrentPrice()))
                .andExpect(jsonPath("$.name").value(stock.getName()))
                //  .andExpect(jsonPath("$.lastUpdate").value(stock.getLastUpdate()))
                .andDo(print());
    }

    @Test
    void getStockById_InvalidStockId() throws Exception {
        long id = 1L;
        when(stockRepository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/stocks/{stockId}", id).header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void getStocks() throws Exception {

        Page<Stock> stocks = new PageImpl<>(new ArrayList<>(Arrays.asList(new Stock(1L, "TTT", 123.11, Timestamp.from(Instant.now()), "girish.jaiswal@gmail.com"),
                new Stock(2L, "SSS", 546.66, Timestamp.from(Instant.now()), "girish.jaiswal@gmail.com"))));

        when(stockRepository.findAll(isA(Pageable.class))).thenReturn(stocks);
        mockMvc.perform(get("/api/stocks").header(HttpHeaders.AUTHORIZATION, "Bearer token"))

                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()").value(stocks.getTotalElements()))
                .andDo(print());

    }

    @Test
    void createStock() throws Exception {
        Stock stock = getDummyStock();
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
        mockMvc.perform(post("/api/stocks").header(HttpHeaders.AUTHORIZATION, "Bearer token").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stock)))
                .andExpect(status().isCreated())
                .andDo(print());

    }

    @Test
    public void createStock_InValidName() throws Exception {
        Stock stock = new Stock(1L, "", 123.11, Timestamp.from(Instant.now()), "user1@test.com");
        mockMvc.perform(post("/api/stocks").header(HttpHeaders.AUTHORIZATION, "Bearer token").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stock)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("name is mandatory."))
                .andDo(print());

    }

    @Test
    public void createStock_NameExist() throws Exception {
        Stock stock = getDummyStock();
        when(stockRepository.save(any(Stock.class))).thenThrow(DataIntegrityViolationException.class);
        mockMvc.perform(post("/api/stocks").header(HttpHeaders.AUTHORIZATION, "Bearer token").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stock)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Name Exist!!"))
                .andDo(print());
    }

    @Test
    void updateStock() throws Exception {
        long id = 1L;
        Timestamp timestamp = Timestamp.from(Instant.now());
        Stock stock = getDummyStock();
        Stock updatedStock = new Stock(id, "TTT", 124.11, timestamp, "user1@test.com");

        when(stockRepository.findById(id)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(updatedStock);

        mockMvc.perform(patch("/api/stocks/{stockId}", id).header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStock)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.currentPrice").value(updatedStock.getCurrentPrice()))
                // .andExpect(jsonPath("$.lastUpdate").value(updatedStock.getLastUpdate()))
                .andDo(print());
    }

    @Test
    void updateStock_InvalidStockId() throws Exception {
        long id = 1L;
        Stock stock = getDummyStock();
        when(stockRepository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(patch("/api/stocks/{stockId}", id).header(HttpHeaders.AUTHORIZATION, "Bearer token").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stock)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void updateStock_InvalidUser() throws Exception {
        long id = 1L;
        Stock stock = new Stock(id, "TTT", 124.11, Timestamp.from(Instant.now()), "girish.jaiswal@gmail.com");

        when(stockRepository.findById(id)).thenReturn(Optional.of(stock));
        mockMvc.perform(patch("/api/stocks/{stockId}", id).header(HttpHeaders.AUTHORIZATION, "Bearer token").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stock)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void deleteStock() throws Exception {
        long id = 1L;
        Stock stock = getDummyStock();
        when(stockRepository.findById(id)).thenReturn(Optional.of(stock));
        doNothing().when(stockRepository).deleteById(id);
        mockMvc.perform(delete("/api/stocks/{stockId}", id).header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void deleteStock_InvalidStockId() throws Exception {
        long id = 1L;
        when(stockRepository.findById(id)).thenReturn(Optional.empty());
        doNothing().when(stockRepository).deleteById(id);
        mockMvc.perform(delete("/api/stocks/{stockId}", id).header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void deleteStock_InvalidUser() throws Exception {
        long id = 1L;
        Stock stock = new Stock(id, "TTT", 124.11, Timestamp.from(Instant.now()), "girish.jaiswal@gmail.com");

        when(stockRepository.findById(id)).thenReturn(Optional.of(stock));
        mockMvc.perform(delete("/api/stocks/{stockId}", id).header(HttpHeaders.AUTHORIZATION, "Bearer token").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stock)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    public Stock getDummyStock() {
        return new Stock(1L, "TTT", 123.11, Timestamp.from(ZonedDateTime.now().toInstant()), "user1@test.com");
    }
}

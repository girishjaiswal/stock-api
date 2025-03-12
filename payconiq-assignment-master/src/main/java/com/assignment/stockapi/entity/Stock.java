package com.assignment.stockapi.entity;

import com.assignment.stockapi.dto.StockDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Timestamp;
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    @Id
    @SequenceGenerator(name = "stockSequence", sequenceName = "stock_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockSequence")
    private Long id;
    @Column(unique = true)
    private String name;
    private Double currentPrice;
    @UpdateTimestamp
    private Timestamp lastUpdate;
    private String userName;
    public Stock(StockDTO stockdto) {
        this.currentPrice = stockdto.getCurrentPrice();
        this.name = stockdto.getName();
    }
}



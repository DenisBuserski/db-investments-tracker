package com.investments.tracker.model;

import com.investments.tracker.enums.PreciousMetalType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;

@Entity
@Table(name = "precious_metals")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PreciousMetals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private PreciousMetalType type;

    private String sellerName;
    private String productName;
    private URL url;
    private LocalDate transactionDate;
    private double sizeInGrams;
    private BigDecimal priceBGN;
    private BigDecimal priceEUR;
    private BigDecimal pricePerGramEUR;
    private BigDecimal pricePerGramOnDateEUR;
    private BigDecimal difference;
}

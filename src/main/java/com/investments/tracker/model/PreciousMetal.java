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
public class PreciousMetal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private PreciousMetalType type;

    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "product_name")
    private String productName;

    private URL url;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "size_in_grams")
    private double sizeInGrams;

    @Column(name = "price_bgn")
    private BigDecimal priceBGN;

    @Column(name = "price_eur")
    private BigDecimal priceEUR;

    @Column(name = "price_per_gram_eur")
    private BigDecimal pricePerGramEUR;

    @Column(name = "price_per_gram_on_date_eur")
    private BigDecimal pricePerGramOnDateEUR;

    private BigDecimal difference;
}

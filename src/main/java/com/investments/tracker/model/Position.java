package com.investments.tracker.model;

import com.investments.tracker.model.enums.ProductType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "positions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "product_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private BigDecimal quantity;

    private BigDecimal lastPrice;

    private BigDecimal totalInvestmentMoney;

    private BigDecimal unrealizedProfitLoss;

    private BigDecimal unrealizedProfitLossPercentage;
}

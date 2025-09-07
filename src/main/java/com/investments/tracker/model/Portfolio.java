package com.investments.tracker.model;

import com.investments.tracker.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "portfolio")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "last_updated")
    private LocalDate lastUpdated;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "invested_money")
    private BigDecimal investedMoney;

    @Column(name = "average_price")
    private BigDecimal averagePrice;

    @Column(name = "dividends_amount")
    private BigDecimal dividendsAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}

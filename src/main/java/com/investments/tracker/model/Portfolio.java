package com.investments.tracker.model;

import com.investments.tracker.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "portfolio")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "last_updated", nullable = false)
    private LocalDate lastUpdated;

    @Column(name = "product_name", nullable = false)
    private String productName;

    private int quantity;

    @Column(name = "invested_money", nullable = false)
    private BigDecimal investedMoney;

    @Column(name = "dividends_amount", nullable = false)
    private BigDecimal dividendsAmount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}

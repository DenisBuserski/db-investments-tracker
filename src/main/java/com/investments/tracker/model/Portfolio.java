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
@Setter
@Builder
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate lastUpdated;
    private String productName;
    private int quantity;
    private BigDecimal investedMoney;
    private BigDecimal averagePrice;
    private BigDecimal dividendsAmount;

    @Enumerated(EnumType.STRING)
    private Status status;
}

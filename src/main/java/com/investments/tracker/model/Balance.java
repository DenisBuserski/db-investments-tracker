package com.investments.tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "balance")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    private BigDecimal balance;

    @Column(name = "total_deposits")
    private BigDecimal totalDeposits;

    @Column(name = "total_withdrawals")
    private BigDecimal totalWithdrawals;

    @Column(name = "total_dividends")
    private BigDecimal totalDividends;

    @Column(name = "total_fees")
    private BigDecimal totalFees;


}

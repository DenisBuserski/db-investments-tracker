package com.investments.tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "portfolio_value_history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PortfolioValueHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "portfolio_value", nullable = false)
    private BigDecimal portfolioValue;

    @Column(name = "portfolio_value_difference_from_last_value", nullable = false)
    private BigDecimal portfolioValueDifferenceFromLastValue;

    @Column(name = "unrealized_portfolio_value", nullable = false)
    private BigDecimal unrealizedPortfolioValue;

    @Column(name = "unrealized_portfolio_value_difference_from_last_value", nullable = false)
    private BigDecimal unrealizedPortfolioValueDifferenceFromLastValue;

}

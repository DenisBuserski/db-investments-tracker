package com.investments.tracker.model;

import com.investments.tracker.model.enums.FeeType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fees")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Fee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate date;

    private FeeType feeType;

    private String description;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private Transaction transaction;

}

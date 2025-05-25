package io.github.andrewrds.fintrack.balance;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table
public class BalanceSnapshot {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private LocalDate balanceDate;
}

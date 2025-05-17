package io.github.andrewrds.fintrack.balance;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.github.andrewrds.fintrack.account.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "UQ_account_date", columnNames = { "account_id", "balanceDate" }))
public class BalanceDatapoint {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "account_id", foreignKey = @ForeignKey(name = "FK_account_id"))
    private Account account;

    @NotNull
    private LocalDate balanceDate;

    @NotNull
    private BigDecimal balance;
}

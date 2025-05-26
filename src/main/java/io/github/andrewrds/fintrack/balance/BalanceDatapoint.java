package io.github.andrewrds.fintrack.balance;

import java.math.BigDecimal;

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
@Table(uniqueConstraints = @UniqueConstraint(name = "UQ_datapoint", columnNames = { "balance_snapshot_id", "account_id" }))
public class BalanceDatapoint {

    @Id
    @GeneratedValue
    private Long id;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "balance_snapshot_id", foreignKey = @ForeignKey(name = "FK_balance_snapshot_id"))
    private BalanceSnapshot balanceSnapshot;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "account_id", foreignKey = @ForeignKey(name = "FK_account_id"))
    private Account account;

    @NotNull
    private BigDecimal balance;

    public Long getId() {
        return id;
    }

    public BalanceSnapshot getBalanceSnapshot() {
        return balanceSnapshot;
    }

    public Account getAccount() {
        return account;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}

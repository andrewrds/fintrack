package io.github.andrewrds.fintrack.balance;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    
    @OneToMany(mappedBy="balanceSnapshot")
    private List<BalanceDatapoint> datapoints;
    
    public Long getId() {
        return id;
    }

    public LocalDate getBalanceDate() {
        return balanceDate;
    }

    public List<BalanceDatapoint> getDatapoints() {
        return datapoints;
    }
}

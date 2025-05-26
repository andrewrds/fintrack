package io.github.andrewrds.fintrack.balance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record BalanceSnapshotResponse(List<Account> accounts, List<Snapshot> snapshots) {
    
    public record Account(long id, String name) {
    }

    public record Snapshot(LocalDate date, List<Datapoint> data) {
    }

    public record Datapoint(BigDecimal balance) {
    }
}

package io.github.andrewrds.fintrack.balance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record BalanceSnapshotResponse(List<LocalDate> dates, List<Provider> providers) {

    public record Provider(long id, String name, List<Account> accounts) {
        public Provider(long id, String name) {
            this(id, name, new ArrayList<>());
        }
    }

    public record Account(long id, String name, List<BigDecimal> balances) {
        public Account(long id, String name) {
            this(id, name, new ArrayList<>());
        }
    }
}

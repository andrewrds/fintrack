package io.github.andrewrds.fintrack.balance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import io.github.andrewrds.fintrack.provider.Provider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class BalanceService {
    @PersistenceContext
    private final EntityManager entityManager;

    public BalanceService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public BalanceSnapshotResponse list() {

        List<Provider> result = entityManager.createQuery("""
                FROM Provider as p
                INNER JOIN FETCH p.accounts as a
                INNER JOIN FETCH a.datapoints as dp""", Provider.class)
                .getResultList();

        var dates = result.stream()
                .flatMap(p -> p.getAccounts().stream())
                .flatMap(a -> a.getDatapoints().stream())
                .map(d -> d.getBalanceDate())
                .sorted()
                .collect(Collectors.toSet());

        var providers = new ArrayList<BalanceSnapshotResponse.Provider>();
        for (var p : result) {
            var provider = new BalanceSnapshotResponse.Provider(p.getId(), p.getName());
            providers.add(provider);

            for (var a : p.getAccounts()) {
                var account = new BalanceSnapshotResponse.Account(a.getId(), a.getName());
                provider.accounts().add(account);

                var dpMap = new HashMap<LocalDate, BalanceDatapoint>();
                for (BalanceDatapoint dp : a.getDatapoints()) {
                    dpMap.put(dp.getBalanceDate(), dp);
                }

                for (LocalDate d : dates) {
                    BalanceDatapoint dp = dpMap.get(d);
                    BigDecimal balance = (dp != null) ? dp.getBalance() : null;
                    account.balances().add(balance);
                }
            }
        }

        return new BalanceSnapshotResponse(new ArrayList<>(dates), providers);
    }
}

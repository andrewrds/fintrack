package io.github.andrewrds.fintrack.balance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.stereotype.Component;

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

        List<BalanceSnapshot> snapshots = entityManager.createQuery("""
                FROM BalanceSnapshot as s
                ORDER BY s.balanceDate""", BalanceSnapshot.class)
                .getResultList();

        var accountList = new LinkedHashSet<BalanceSnapshotResponse.Account>();
        for (BalanceSnapshot s : snapshots) {
            for (BalanceDatapoint datapoint : s.getDatapoints()) {
                var a = datapoint.getAccount();
                var account = new BalanceSnapshotResponse.Account(a.getId(), a.getName());
                accountList.add(account);
            }
        }

        var snapshotResponse = new ArrayList<BalanceSnapshotResponse.Snapshot>();
        for (BalanceSnapshot s : snapshots) {
            var dpMap = new HashMap<Long, BalanceDatapoint>();

            for (BalanceDatapoint datapoint : s.getDatapoints()) {
                dpMap.put(datapoint.getAccount().getId(), datapoint);
            }

            var datapoints = new ArrayList<BalanceSnapshotResponse.Datapoint>();
            for (var account : accountList) {
                var datapoint = dpMap.get(account.id());
                BigDecimal balance = (datapoint != null)
                        ? datapoint.getBalance()
                        : BigDecimal.ZERO;
                datapoints.add(new BalanceSnapshotResponse.Datapoint(balance));
            }

            var snapshot = new BalanceSnapshotResponse.Snapshot(s.getBalanceDate(), datapoints);
            snapshotResponse.add(snapshot);
        }

        return new BalanceSnapshotResponse(new ArrayList<>(accountList), snapshotResponse);
    }
}

package io.github.andrewrds.fintrack.account;

import org.springframework.stereotype.Component;

import io.github.andrewrds.fintrack.provider.ProviderService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class TransactedAccountService {
    @PersistenceContext
    private final EntityManager entityManager;

    private final ProviderService providerService;

    public TransactedAccountService(EntityManager entityManager, ProviderService providerService) {
        this.entityManager = entityManager;
        this.providerService = providerService;
    }

    @Transactional
    public Account create(long providerId, String accountName) {
        var provider = providerService.find(providerId);
        var account = new Account(provider, accountName);
        entityManager.persist(account);
        return account;
    }
}

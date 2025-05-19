package io.github.andrewrds.fintrack.account;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

import io.github.andrewrds.fintrack.provider.ProviderService;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class TransactedAccountService {
    @PersistenceContext
    private final Session session;

    private final ProviderService providerService;

    public TransactedAccountService(Session session, ProviderService providerService) {
        this.session = session;
        this.providerService = providerService;
    }

    @Transactional
    public Account create(long providerId, String accountName) {
        var provider = providerService.find(providerId);
        var account = new Account(provider, accountName);
        session.persist(account);
        return account;
    }
}

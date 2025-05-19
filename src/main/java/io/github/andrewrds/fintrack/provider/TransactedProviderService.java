package io.github.andrewrds.fintrack.provider;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class TransactedProviderService {
    @PersistenceContext
    private final EntityManager entityManager;

    public TransactedProviderService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Provider create(String name) {
        Provider provider = new Provider(name);
        entityManager.persist(provider);
        return provider;
    }
}

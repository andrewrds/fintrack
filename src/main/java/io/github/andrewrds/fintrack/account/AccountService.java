package io.github.andrewrds.fintrack.account;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class AccountService {
    @PersistenceContext
    private final EntityManager entityManager;

    private final TransactedAccountService transacted;

    public AccountService(EntityManager entityManager, TransactedAccountService transacted) {
        this.entityManager = entityManager;
        this.transacted = transacted;
    }

    public Account create(long providerId, String accountName) {
        try {
            return transacted.create(providerId, accountName);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new DuplicateAccountNameException();
            } else {
                throw e;
            }
        }
    }

    @Transactional
    public void delete(long accountId) {
        entityManager.createQuery("""
                DELETE FROM Account
                WHERE id = :id""")
                .setParameter("id", accountId)
                .executeUpdate();
    }

    public List<AccountResponse> listForProvider(long providerId) {
        return entityManager.createQuery("""
                FROM Account as a
                WHERE a.provider.id = :providerId
                ORDER BY a.name""", Account.class)
                .setParameter("providerId", providerId)
                .getResultList().stream()
                .map(a -> new AccountResponse(a.getId(), a.getName()))
                .toList();
    }
}

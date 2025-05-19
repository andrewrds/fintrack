package io.github.andrewrds.fintrack.account;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import io.github.andrewrds.fintrack.provider.Provider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class AccountService {
    @PersistenceContext
    private final EntityManager entityManager;

    @PersistenceContext
    private final Session session;

    private final TransactedAccountService transacted;

    public AccountService(EntityManager entityManager, Session session, TransactedAccountService transacted) {
        this.entityManager = entityManager;
        this.session = session;
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
        session.createMutationQuery("""
                DELETE FROM Account
                WHERE id = :id""")
                .setParameter("id", accountId)
                .executeUpdate();
    }

    public List<Account> listForProvider(Provider provider) {
        return session.createQuery("""
                FROM Account as a
                WHERE a.provider.id = :providerId
                ORDER BY a.name""", Account.class)
                .setParameter("providerId", provider.getId())
                .getResultList();
    }
}

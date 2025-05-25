package io.github.andrewrds.fintrack.provider;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Component
public class ProviderService {
    @PersistenceContext
    private final EntityManager entityManager;

    private final TransactedProviderService transacted;

    public ProviderService(EntityManager entityManager, TransactedProviderService transacted) {
        this.entityManager = entityManager;
        this.transacted = transacted;
    }

    public Provider create(String name) {
        try {
            return transacted.create(name);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new DuplicateProviderNameException();
            } else {
                throw e;
            }
        }
    }

    public void delete(long id) {
        try {
            transacted.delete(id);
        } catch (ConstraintViolationException e) {
            throw new DeleteProviderException();
        }
    }

    public List<Provider> list() {
        return entityManager.createQuery("""
                FROM Provider as p
                ORDER BY p.name""", Provider.class)
                .getResultList();
    }

    public Provider find(long id) {
        try {
            return entityManager.createQuery("""
                    FROM Provider as p
                    WHERE p.id = :id""", Provider.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new ProviderNotFoundException();
        }
    }
}

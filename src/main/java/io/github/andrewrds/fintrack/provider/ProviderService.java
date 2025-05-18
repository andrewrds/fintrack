package io.github.andrewrds.fintrack.provider;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class ProviderService {
	@PersistenceContext
	private final Session session;

	private final TransactedProviderService transacted;

	public ProviderService(Session session, TransactedProviderService transacted) {
		this.session = session;
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

	@Transactional
	public void delete(long id) {
		session.createMutationQuery("""
				DELETE FROM Provider
				WHERE id = :id""")
				.setParameter("id", id)
				.executeUpdate();
	}

	public List<Provider> list() {
		return session.createQuery("""
				FROM Provider as p
				ORDER BY p.name""", Provider.class)
				.getResultList();
	}

	public Provider find(long id) {
		Provider provider = session.createQuery("""
				FROM Provider as p
				WHERE p.id = :id""", Provider.class)
				.setParameter("id", id)
				.getSingleResultOrNull();

		if (provider == null) {
			throw new ProviderNotFoundException();
		}

		return provider;
	}
}

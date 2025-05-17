package io.github.andrewrds.fintrack.provider;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
public class ProviderService {
	@PersistenceContext
	private final EntityManager entityManager;

	@PersistenceContext
	private final Session session;

	public ProviderService(EntityManager entityManager, Session session) {
		this.entityManager = entityManager;
		this.session = session;
	}

	@Transactional
	public Provider create(String name) {
		Provider provider = new Provider(name);
		entityManager.persist(provider);
		return provider;
	}

	@Transactional
	public Provider delete(long id) {
		Provider provider = find(id);
		entityManager.remove(provider);
		return provider;
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

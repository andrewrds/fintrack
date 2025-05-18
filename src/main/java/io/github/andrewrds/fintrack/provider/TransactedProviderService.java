package io.github.andrewrds.fintrack.provider;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class TransactedProviderService {
	@PersistenceContext
	private final Session session;

	public TransactedProviderService(Session session) {
		this.session = session;
	}

	@Transactional
	public Provider create(String name) {
		Provider provider = new Provider(name);
		session.persist(provider);
		return provider;
	}
}

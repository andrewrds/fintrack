package io.github.andrewrds.fintrack.account;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

import io.github.andrewrds.fintrack.provider.Provider;
import io.github.andrewrds.fintrack.provider.ProviderService;

@Component
public class AccountService {
	@PersistenceContext
	private final EntityManager entityManager;

	@PersistenceContext
	private final Session session;

	private final ProviderService providerService;

	public AccountService(EntityManager entityManager, Session session, ProviderService providerService) {
		this.entityManager = entityManager;
		this.session = session;
		this.providerService = providerService;
	}

	@Transactional
	public void create(long providerId, String accountName) {
		Provider provider = providerService.find(providerId);
		entityManager.persist(new Account(provider, accountName));
	}

	@Transactional
	public void delete(long providerId, long accountId) {
		Account account = find(providerId, accountId);
		entityManager.remove(account);
	}

	public List<Account> listForProvider(Provider provider) {
		return session.createQuery("""
				FROM Account as a
				WHERE a.provider.id = :providerId
				ORDER BY a.name""", Account.class)
				.setParameter("providerId", provider.getId())
				.getResultList();
	}

	private Account find(long providerId, long accountId) {
		return session
				.createQuery("""
						FROM Account as a
						WHERE a.provider.id = :providerId
						AND a.id = :accountId""",
						Account.class)
				.setParameter("providerId", providerId)
				.setParameter("accountId", accountId)
				.getSingleResultOrNull();
	}
}

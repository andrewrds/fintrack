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
	public Account create(long providerId, String accountName) {
		var provider = providerService.find(providerId);
		var account = new Account(provider, accountName);
		entityManager.persist(account);
		return account;
	}

	@Transactional
	public Account delete(long accountId) {
		var account = find(accountId);
		entityManager.remove(account);
		return account;
	}

	public List<Account> listForProvider(Provider provider) {
		return session.createQuery("""
				FROM Account as a
				WHERE a.provider.id = :providerId
				ORDER BY a.name""", Account.class)
				.setParameter("providerId", provider.getId())
				.getResultList();
	}

	private Account find(long accountId) {
		var account = session
				.createQuery("""
						FROM Account as a
						WHERE a.id = :accountId""",
						Account.class)
				.setParameter("accountId", accountId)
				.getSingleResultOrNull();

		if (account == null) {
			throw new AccountNotFoundException();
		}

		return account;
	}
}

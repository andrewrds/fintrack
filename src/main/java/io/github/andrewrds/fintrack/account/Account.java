package io.github.andrewrds.fintrack.account;

import io.github.andrewrds.fintrack.provider.Provider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "UQ_provider_name", columnNames = { "provider_id", "name" }))
public class Account {
    public static final int NAME_MAX_LENGTH = 100;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "provider_id", foreignKey = @ForeignKey(name = "FK_provider_id"))
    private Provider provider;

    @NotNull
    @Column(length = NAME_MAX_LENGTH)
    @Size(min = 0, max = Account.NAME_MAX_LENGTH)
    private String name;

    public Account() {
    }

    public Account(Provider provider, String name) {
        this.provider = provider;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
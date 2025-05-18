package io.github.andrewrds.fintrack.provider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "UQ_name", columnNames = { "name" }))
public class Provider {
    public static final int NAME_MAX_LENGTH = 100;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(length = NAME_MAX_LENGTH)
    private String name;

    public Provider() {
    }

    public Provider(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
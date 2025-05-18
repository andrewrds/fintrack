package io.github.andrewrds.fintrack.provider;

import jakarta.validation.constraints.NotNull;

public class DeleteProviderRequest {
    @NotNull
    private Long id;

    public DeleteProviderRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

package io.github.andrewrds.fintrack.account;

import jakarta.validation.constraints.NotNull;

public class DeleteAccountRequest {
    @NotNull
    private Long id;

    public DeleteAccountRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

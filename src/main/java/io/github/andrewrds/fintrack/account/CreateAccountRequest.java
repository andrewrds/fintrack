package io.github.andrewrds.fintrack.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateAccountRequest {
    @NotNull
    private Long providerId;

    @NotNull
    @Size(min = 0, max = Account.NAME_MAX_LENGTH)
    private String name;

    public CreateAccountRequest(Long providerId, String name) {
        this.providerId = providerId;
        this.name = name;
    }

    public Long getProviderId() {
        return providerId;
    }

    public String getName() {
        return name;
    }
}

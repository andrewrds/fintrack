package io.github.andrewrds.fintrack.account;

import jakarta.validation.constraints.NotNull;

public class CreateAccountRequest {
    @NotNull
    private Long providerId;

    @NotNull
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

package io.github.andrewrds.fintrack.provider;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateProviderRequest {
    @NotNull
    @Size(min = 0, max = Provider.NAME_MAX_LENGTH)
    private String name;

    public CreateProviderRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

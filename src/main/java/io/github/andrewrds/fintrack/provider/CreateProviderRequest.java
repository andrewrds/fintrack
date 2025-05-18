package io.github.andrewrds.fintrack.provider;

import jakarta.validation.constraints.NotNull;

public class CreateProviderRequest {
	@NotNull
	private String name;
	
    public CreateProviderRequest(String name) {
        this.name = name;
    }
    
	public String getName() {
        return name;
    }
}

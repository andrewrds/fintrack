package io.github.andrewrds.fintrack.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.github.andrewrds.fintrack.provider.CreateProviderRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AccountRestControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreate() throws Exception {
        var providerName = UUID.randomUUID().toString();
        var name = UUID.randomUUID().toString();
        
        var providerJson = restTemplate.postForObject(
                "http://localhost:" + port + "/provider/create",
                new CreateProviderRequest(providerName),
                String.class);
        var providerId = new JSONObject(providerJson).getLong("id");

        var result = restTemplate.postForObject(
                "http://localhost:" + port + "/account/create",
                new CreateAccountRequest(providerId, name),
                String.class);

        var json = new JSONObject(result);
        assertTrue(json.has("id"), "json missing id: " + json);
        assertTrue(json.has("name"), "json missing name: " + json);
        assertEquals(name, json.getString("name"));
    }

    @Test
    void testCreate_existing() throws Exception {
        var providerName = UUID.randomUUID().toString();
        var name = UUID.randomUUID().toString();
        
        var providerJson = restTemplate.postForObject(
                "http://localhost:" + port + "/provider/create",
                new CreateProviderRequest(providerName),
                String.class);
        var providerId = new JSONObject(providerJson).getLong("id");
        
        restTemplate.postForObject(
                "http://localhost:" + port + "/account/create",
                new CreateAccountRequest(providerId, name),
                String.class);

        var result = restTemplate.postForObject(
                "http://localhost:" + port + "/account/create",
                new CreateAccountRequest(providerId, name),
                String.class);

        assertEquals("""
                {"error":"An account with the same name already exists for the provider"}""", result);
    }

    @Test
    void testDelete() throws Exception {
        var providerName = UUID.randomUUID().toString();
        var name = UUID.randomUUID().toString();
        
        var providerJson = restTemplate.postForObject(
                "http://localhost:" + port + "/provider/create",
                new CreateProviderRequest(providerName),
                String.class);
        var providerId = new JSONObject(providerJson).getLong("id");

        var result = restTemplate.postForObject(
                "http://localhost:" + port + "/account/create",
                new CreateAccountRequest(providerId, name),
                String.class);
        
        var id = new JSONObject(result).getLong("id");

        var json = restTemplate.postForObject(
                "http://localhost:" + port + "/account/delete",
                new DeleteAccountRequest(id),
                String.class);

        assertEquals("""
                {"message":"Account deleted"}""", json);
    }
}

package io.github.andrewrds.fintrack.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
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

    private String url;
    
    private long providerId;

    @BeforeEach
    void setup() throws Exception {
        url = "http://localhost:" + port;
        
        var providerName = UUID.randomUUID().toString();
        var providerJson = restTemplate.postForObject(
                url + "/provider/create",
                new CreateProviderRequest(providerName),
                String.class);
        providerId = new JSONObject(providerJson).getLong("id");
    }

    @Test
    void testCreate() throws Exception {
        var name = UUID.randomUUID().toString();

        var result = restTemplate.postForObject(
                url + "/account/create",
                new CreateAccountRequest(providerId, name),
                String.class);

        var json = new JSONObject(result);
        assertTrue(json.has("id"), "json missing id: " + json);
        assertTrue(json.has("name"), "json missing name: " + json);
        assertEquals(name, json.getString("name"));
    }

    @Test
    void testCreate_existing() throws Exception {
        var name = UUID.randomUUID().toString();

        restTemplate.postForObject(
                url + "/account/create",
                new CreateAccountRequest(providerId, name),
                String.class);

        var result = restTemplate.postForObject(
                url + "/account/create",
                new CreateAccountRequest(providerId, name),
                String.class);

        assertEquals("""
                {"error":"An account with the same name already exists for the provider"}""", result);
    }

    @Test
    void testDelete() throws Exception {
        var name = UUID.randomUUID().toString();

        var result = restTemplate.postForObject(
                url + "/account/create",
                new CreateAccountRequest(providerId, name),
                String.class);

        var id = new JSONObject(result).getLong("id");

        var json = restTemplate.postForObject(
                url + "/account/delete",
                new DeleteAccountRequest(id),
                String.class);

        assertEquals("""
                {"message":"Account deleted"}""", json);
    }

    @Test
    void listForProvider() throws Exception {
        var name1 = "z_" + UUID.randomUUID().toString();
        var name2 = "a_" + UUID.randomUUID().toString();
        var name3 = "r_" + UUID.randomUUID().toString();

        restTemplate.postForObject(
                url + "/account/create",
                new CreateAccountRequest(providerId, name1),
                String.class);
        
        restTemplate.postForObject(
                url + "/account/create",
                new CreateAccountRequest(providerId, name2),
                String.class);
        
        restTemplate.postForObject(
                url + "/account/create",
                new CreateAccountRequest(providerId, name3),
                String.class);
        
        var result = restTemplate.getForObject(
                url + "/account/listForProvider?providerId=" + providerId,
                String.class);

        var json = new JSONArray(result);
        
        assertEquals(3, json.length());
        assertEquals(name2, json.getJSONObject(0).getString("name"));
        assertEquals(name3, json.getJSONObject(1).getString("name"));
        assertEquals(name1, json.getJSONObject(2).getString("name"));
    }
}

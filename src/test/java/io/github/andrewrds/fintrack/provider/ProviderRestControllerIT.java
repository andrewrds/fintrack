package io.github.andrewrds.fintrack.provider;

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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ProviderRestControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreate() throws Exception {
        var name = UUID.randomUUID().toString();

        var result = restTemplate.postForObject(
                "http://localhost:" + port + "/provider/create",
                new CreateProviderRequest(name),
                String.class);

        var json = new JSONObject(result);
        assertTrue(json.has("id"), "json missing id: " + json);
        assertTrue(json.has("name"), "json missing name: " + json);
        assertEquals(name, json.getString("name"));
    }

    @Test
    void testCreate_existing() {
        var name = UUID.randomUUID().toString();

        restTemplate.postForObject(
                "http://localhost:" + port + "/provider/create",
                new CreateProviderRequest(name),
                String.class);

        String result = restTemplate.postForObject(
                "http://localhost:" + port + "/provider/create",
                new CreateProviderRequest(name),
                String.class);

        assertEquals("""
                {"error":"A provider with the same name already exists"}""", result);
    }

    @Test
    void testDelete() throws Exception {
        var name = UUID.randomUUID().toString();

        var createJson = restTemplate.postForObject(
                "http://localhost:" + port + "/provider/create",
                new CreateProviderRequest(name),
                String.class);
        var id = new JSONObject(createJson).getLong("id");

        var json = restTemplate.postForObject(
                "http://localhost:" + port + "/provider/delete",
                new DeleteProviderRequest(id),
                String.class);

        assertEquals("""
                {"message":"Provider deleted"}""", json);
    }
}

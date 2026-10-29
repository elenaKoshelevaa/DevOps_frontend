package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
public class ProxyController {

    private final RestClient client;

    public ProxyController(@Value("${backend.url:http://localhost:8081}") String backendUrl) {
        this.client = RestClient.create(backendUrl);
    }

    @PostMapping(value = "/api/data", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> save(@RequestBody String text) {
        String body = client.post()
                .uri("/api/data")
                .contentType(MediaType.parseMediaType("text/plain;charset=UTF-8"))
                .body(text)
                .retrieve()
                .body(String.class);
        return ResponseEntity.ok(body);
    }

    @GetMapping(value = "/api/data", produces = "text/plain;charset=UTF-8")
    public String read(@RequestParam(required = false, defaultValue = "") String filter) {
        return client.get()
                .uri(b -> b.path("/api/data").queryParam("filter", filter).build())
                .retrieve()
                .body(String.class);
    }
}

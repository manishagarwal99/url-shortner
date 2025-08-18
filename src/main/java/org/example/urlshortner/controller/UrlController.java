package org.example.urlshortner.controller;

import org.example.urlshortner.service.UrlService;
import org.example.urlshortner.service.UrlServiceMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
public class UrlController {

    private final UrlServiceMap urlService;

    public UrlController(UrlServiceMap urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestBody Map<String, String> request) {
        String shortCode = urlService.shortenUrl(request.get("url"));
        return ResponseEntity.ok("http://localhost:8080/" + shortCode);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        Optional<String> originalUrl = urlService.getOriginalUrl(shortCode);
        return originalUrl.<ResponseEntity<Void>>map(s -> ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(s))
            .build()).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}


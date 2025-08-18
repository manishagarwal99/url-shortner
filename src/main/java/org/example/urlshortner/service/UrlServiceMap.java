package org.example.urlshortner.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UrlServiceMap {

    //use a Map to store URL mappings

    private final Map<String, String> urlMap = new HashMap<>();

    // Method to shorten a URL
    public String shortenUrl(String longUrl) {
        // Generate a unique short code (for simplicity, using the URL itself as the key)
        String shortCode = Long.toHexString(longUrl.hashCode());
        urlMap.put(shortCode, longUrl);
        return shortCode;
    }

    // Method to retrieve the original URL from a short code
    public Optional<String> getOriginalUrl(String shortCode) {
        return urlMap.get(shortCode).describeConstable();
    }
}

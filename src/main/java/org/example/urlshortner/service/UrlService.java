package org.example.urlshortner.service;

import org.example.urlshortner.repository.UrlMappingRepository;
import org.example.urlshortner.model.UrlMapping;
import org.example.urlshortner.service.encoding.UrlCodeStrategy;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    private final UrlMappingRepository repository;
    private final UrlCodeStrategy codecStrategy;

    public UrlService(UrlMappingRepository repository, UrlCodeStrategy codecStrategy) {
        this.repository = repository;
        this.codecStrategy = codecStrategy;
    }

    public String shortenUrl(String originalUrl) {
        // Step 1: Save URL (get ID)
        UrlMapping mapping = new UrlMapping();
        mapping.setLongUrl(originalUrl);
        mapping.setClickCount(0L);
        mapping.setExpiryAt((System.currentTimeMillis() / 1000) + (7 * 24 * 60 * 60));
        mapping = repository.save(mapping);

        // Step 2: Encode using selected strategy
        String code = codecStrategy.encode(mapping);

        // Step 3: Save the short code
        mapping.setShortCode(code);
        repository.save(mapping);

        return code;
    }

    public String getOriginalUrl(String shortCode) {
        long id = codecStrategy.decode(shortCode);
        return repository.findById(id)
            .map(UrlMapping::getLongUrl)
            .orElseThrow(() -> new RuntimeException("URL not found"));
    }
}



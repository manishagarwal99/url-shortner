package org.example.urlshortner.service.encoding;

import org.example.urlshortner.model.UrlMapping;

public interface UrlCodeStrategy {
    String encode(UrlMapping urlMapping);
    long decode(String shortCode);

}


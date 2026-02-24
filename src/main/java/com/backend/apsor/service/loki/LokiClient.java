package com.backend.apsor.service.loki;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service
public class LokiClient {

    private final WebClient webClient;
    private final URI baseUri;

    public LokiClient(@Value("${loki.url:}") String lokiUrl) {
        // fallback ALWAYS includes port 3100 (so you never hit :80)
        String base = (lokiUrl == null || lokiUrl.isBlank())
                ? "http://127.0.0.1:3100"
                : lokiUrl.trim();

        URI uri = URI.create(base);
        // if user accidentally sets http://localhost (no port), force 3100
        if (uri.getPort() == -1) {
            uri = UriComponentsBuilder.fromUri(uri).port(3100).build().toUri();
        }

        this.baseUri = uri;
        this.webClient = WebClient.builder().build();

        System.out.println(">>> Using Loki baseUri = " + this.baseUri);
    }

    public String queryRange(String logql, long startNs, long endNs, int limit) {

        // Build FULL URL: http://127.0.0.1:3100/loki/api/v1/query_range?... (absolute)
        // encode() will safely encode { } | " and spaces inside query param
        URI uri = UriComponentsBuilder.fromUri(baseUri)
                .path("/loki/api/v1/query_range")
                .queryParam("query", logql)
                .queryParam("start", startNs)
                .queryParam("end", endNs)
                .queryParam("limit", limit)
                .queryParam("direction", "BACKWARD")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
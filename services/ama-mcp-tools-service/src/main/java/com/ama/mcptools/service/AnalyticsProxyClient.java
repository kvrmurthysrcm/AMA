package com.ama.mcptools.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * Thin HTTP client that forwards validated tool requests to the Analytics service.
 *
 * This keeps the MCP Tool service decoupled from analytics implementation details.
 * Today it uses HTTP forwarding; later you can replace this with direct service calls
 * if both modules are deployed in the same application.
 */
@Component
public class AnalyticsProxyClient {

    private final RestClient restClient;

    public AnalyticsProxyClient(@Value("${ama.analytics.base-url}") String analyticsBaseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(analyticsBaseUrl)
                .build();
    }

    public ResponseEntity<Object> post(String endpointPath, Map<String, Object> body) {
        return restClient.post()
                .uri(endpointPath)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body == null ? Map.of() : body)
                .retrieve()
                .toEntity(Object.class);
    }
}

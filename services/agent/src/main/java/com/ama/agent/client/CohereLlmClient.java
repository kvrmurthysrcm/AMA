package com.ama.agent.client;

import com.ama.agent.config.LlmProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class CohereLlmClient implements LlmClient {

    private final LlmProperties properties;

    public CohereLlmClient(LlmProperties properties) {
        this.properties = properties;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String generate(String systemPrompt, String userPrompt) {
        var cfg = properties.getCohere();
        if (cfg.getApiKey() == null || cfg.getApiKey().isBlank()) {
            throw new IllegalStateException("Cohere API key is missing");
        }

        RestClient client = RestClient.builder()
                .baseUrl(cfg.getBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + cfg.getApiKey())
                .build();

        Map<String, Object> body = Map.of(
                "model", cfg.getModel(),
                "temperature", properties.getTemperature(),
                "max_tokens", properties.getMaxTokens(),
                "message", userPrompt,
                "preamble", systemPrompt
        );

        Map<String, Object> response = client.post()
                .uri("/v1/chat")
                .body(body)
                .retrieve()
                .body(Map.class);

        Object text = response.get("text");
        if (text == null) {
            throw new IllegalStateException("Cohere returned no text");
        }
        return text.toString();
    }
}

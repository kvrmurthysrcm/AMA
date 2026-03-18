package com.ama.agent.client;

import com.ama.agent.config.LlmProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class AzureOpenAiLlmClient implements LlmClient {

    private final LlmProperties properties;

    public AzureOpenAiLlmClient(LlmProperties properties) {
        this.properties = properties;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String generate(String systemPrompt, String userPrompt) {
        var cfg = properties.getAzureOpenai();
        if (cfg.getEndpoint() == null || cfg.getApiKey() == null || cfg.getDeployment() == null) {
            throw new IllegalStateException("Azure OpenAI configuration is incomplete");
        }

        RestClient client = RestClient.builder()
                .baseUrl(cfg.getEndpoint())
                .defaultHeader("api-key", cfg.getApiKey())
                .build();

        Map<String, Object> body = Map.of(
                "temperature", properties.getTemperature(),
                "max_tokens", properties.getMaxTokens(),
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                )
        );

        String uri = "/openai/deployments/" + cfg.getDeployment() + "/chat/completions?api-version=" + cfg.getApiVersion();

        Map<String, Object> response = client.post()
                .uri(uri)
                .body(body)
                .retrieve()
                .body(Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        if (choices == null || choices.isEmpty()) {
            throw new IllegalStateException("Azure OpenAI returned no choices");
        }
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        return message.get("content").toString();
    }
}

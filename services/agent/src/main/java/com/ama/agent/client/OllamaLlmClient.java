package com.ama.agent.client;

import com.ama.agent.config.LlmProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OllamaLlmClient implements LlmClient {

    private final LlmProperties properties;

    public OllamaLlmClient(LlmProperties properties) {
        this.properties = properties;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String generate(String systemPrompt, String userPrompt) {
        var cfg = properties.getOllama();

        RestClient client = RestClient.builder()
                .baseUrl(cfg.getBaseUrl())
                .build();

        Map<String, Object> options = new HashMap<>();
        var ollamaOptions = cfg.getOptions();
        if (ollamaOptions.getTemperature() != null) {
            options.put("temperature", ollamaOptions.getTemperature());
        } else {
            options.put("temperature", properties.getTemperature());
        }
        if (ollamaOptions.getNumPredict() != null) {
            options.put("num_predict", ollamaOptions.getNumPredict());
        } else {
            options.put("num_predict", properties.getMaxTokens());
        }
        if (ollamaOptions.getTopP() != null) options.put("top_p", ollamaOptions.getTopP());
        if (ollamaOptions.getTopK() != null) options.put("top_k", ollamaOptions.getTopK());
        if (ollamaOptions.getRepeatPenalty() != null) options.put("repeat_penalty", ollamaOptions.getRepeatPenalty());
        if (ollamaOptions.getSeed() != null) options.put("seed", ollamaOptions.getSeed());
        if (ollamaOptions.getNumCtx() != null) options.put("num_ctx", ollamaOptions.getNumCtx());
        if (ollamaOptions.getStop() != null) options.put("stop", ollamaOptions.getStop());

        Map<String, Object> body = Map.of(
                "model", cfg.getModel(),
                "stream", false,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "options", options
        );

        Map<String, Object> response = client.post()
                .uri("/api/chat")
                .body(body)
                .retrieve()
                .body(Map.class);

        Map<String, Object> message = (Map<String, Object>) response.get("message");
        if (message == null || message.get("content") == null) {
            throw new IllegalStateException("Ollama returned no message content");
        }
        return message.get("content").toString();
    }
}

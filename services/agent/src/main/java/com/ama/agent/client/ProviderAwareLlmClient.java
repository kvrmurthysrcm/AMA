package com.ama.agent.client;

import com.ama.agent.config.LlmProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class ProviderAwareLlmClient implements LlmClient {

    private final LlmProperties properties;
    private final OpenAiLlmClient openAiLlmClient;
    private final AzureOpenAiLlmClient azureOpenAiLlmClient;
    private final CohereLlmClient cohereLlmClient;
    private final OllamaLlmClient ollamaLlmClient;

    public ProviderAwareLlmClient(
            LlmProperties properties,
            OpenAiLlmClient openAiLlmClient,
            AzureOpenAiLlmClient azureOpenAiLlmClient,
            CohereLlmClient cohereLlmClient,
            OllamaLlmClient ollamaLlmClient) {
        this.properties = properties;
        this.openAiLlmClient = openAiLlmClient;
        this.azureOpenAiLlmClient = azureOpenAiLlmClient;
        this.cohereLlmClient = cohereLlmClient;
        this.ollamaLlmClient = ollamaLlmClient;
    }

    @Override
    public String generate(String systemPrompt, String userPrompt) {
        return switch (properties.normalizedProvider()) {
            case "openai" -> openAiLlmClient.generate(systemPrompt, userPrompt);
            case "azureopenai" -> azureOpenAiLlmClient.generate(systemPrompt, userPrompt);
            case "cohere" -> cohereLlmClient.generate(systemPrompt, userPrompt);
            case "ollama" -> ollamaLlmClient.generate(systemPrompt, userPrompt);
            default -> throw new IllegalArgumentException("Unsupported LLM provider: " + properties.getProvider() + ". Supported values: openai, azureOpenai, cohere, ollama");
        };
    }
}

package com.ama.agent.client;

public interface LlmClient {
    String generate(String systemPrompt, String userPrompt);
}

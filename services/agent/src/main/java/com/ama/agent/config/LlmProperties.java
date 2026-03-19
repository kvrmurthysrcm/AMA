package com.ama.agent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Locale;

@ConfigurationProperties(prefix = "llm")
public class LlmProperties {

    private String provider = "openai";
    private Double temperature = 0.2;
    private Integer maxTokens = 500;
    private Openai openai = new Openai();
    private AzureOpenai azureOpenai = new AzureOpenai();
    private Cohere cohere = new Cohere();
    private Ollama ollama = new Ollama();

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    public Integer getMaxTokens() { return maxTokens; }
    public void setMaxTokens(Integer maxTokens) { this.maxTokens = maxTokens; }
    public Openai getOpenai() { return openai; }
    public void setOpenai(Openai openai) { this.openai = openai; }
    public AzureOpenai getAzureOpenai() { return azureOpenai; }
    public void setAzureOpenai(AzureOpenai azureOpenai) { this.azureOpenai = azureOpenai; }
    public Cohere getCohere() { return cohere; }
    public void setCohere(Cohere cohere) { this.cohere = cohere; }
    public Ollama getOllama() { return ollama; }
    public void setOllama(Ollama ollama) { this.ollama = ollama; }

    public String normalizedProvider() {
        if (provider == null || provider.isBlank()) {
            return "openai";
        }
        String normalized = provider.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "openai", "open-ai" -> "openai";
            case "azureopenai", "azure-openai", "azure_openai", "azure" -> "azureopenai";
            case "cohere" -> "cohere";
            case "ollama" -> "ollama";
            default -> normalized;
        };
    }

    public static class Openai {
        private String baseUrl = "https://api.openai.com";
        private String apiKey;
        private String model = "gpt-4o-mini";
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
    }

    public static class AzureOpenai {
        private String endpoint;
        private String apiKey;
        private String deployment;
        private String apiVersion = "2024-02-15-preview";
        public String getEndpoint() { return endpoint; }
        public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
        public String getDeployment() { return deployment; }
        public void setDeployment(String deployment) { this.deployment = deployment; }
        public String getApiVersion() { return apiVersion; }
        public void setApiVersion(String apiVersion) { this.apiVersion = apiVersion; }
    }

    public static class Cohere {
        private String baseUrl = "https://api.cohere.com";
        private String apiKey;
        private String model = "command-r";
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
    }

    public static class Ollama {
        private String baseUrl = "http://localhost:11434";
        private String model = "gemma3:4b";
        private Integer timeoutMs = 120000;
        private Options options = new Options();

        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public Integer getTimeoutMs() { return timeoutMs; }
        public void setTimeoutMs(Integer timeoutMs) { this.timeoutMs = timeoutMs; }
        public Options getOptions() { return options; }
        public void setOptions(Options options) { this.options = options; }

        public static class Options {
            private Double temperature;
            private Integer numPredict;
            private Double topP;
            private Integer topK;
            private Double repeatPenalty;
            private Integer seed;
            private Integer numCtx;
            private List<String> stop;

            public Double getTemperature() { return temperature; }
            public void setTemperature(Double temperature) { this.temperature = temperature; }
            public Integer getNumPredict() { return numPredict; }
            public void setNumPredict(Integer numPredict) { this.numPredict = numPredict; }
            public Double getTopP() { return topP; }
            public void setTopP(Double topP) { this.topP = topP; }
            public Integer getTopK() { return topK; }
            public void setTopK(Integer topK) { this.topK = topK; }
            public Double getRepeatPenalty() { return repeatPenalty; }
            public void setRepeatPenalty(Double repeatPenalty) { this.repeatPenalty = repeatPenalty; }
            public Integer getSeed() { return seed; }
            public void setSeed(Integer seed) { this.seed = seed; }
            public Integer getNumCtx() { return numCtx; }
            public void setNumCtx(Integer numCtx) { this.numCtx = numCtx; }
            public List<String> getStop() { return stop; }
            public void setStop(List<String> stop) { this.stop = stop; }
        }
    }
}

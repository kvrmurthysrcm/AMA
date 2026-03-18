package com.ama.agent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AgentProperties {

    private Tools tools = new Tools();

    public Tools getTools() { return tools; }
    public void setTools(Tools tools) { this.tools = tools; }

    public static class Tools {
        private String baseUrl = "http://localhost:8082";
        private String listPath = "/api/mcp/v1/tools/list";
        private String callPath = "/api/mcp/v1/tools/call";

        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        public String getListPath() { return listPath; }
        public void setListPath(String listPath) { this.listPath = listPath; }
        public String getCallPath() { return callPath; }
        public void setCallPath(String callPath) { this.callPath = callPath; }
    }
}

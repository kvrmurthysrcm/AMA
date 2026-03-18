package com.ama.agent.model;

import java.util.Map;

public class ToolDefinition {
    private String name;
    private String title;
    private String description;
    private String endpointPath;
    private String method;
    private String preferredChart;
    private boolean enabled;
    private Map<String, Object> inputSchema;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEndpointPath() { return endpointPath; }
    public void setEndpointPath(String endpointPath) { this.endpointPath = endpointPath; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getPreferredChart() { return preferredChart; }
    public void setPreferredChart(String preferredChart) { this.preferredChart = preferredChart; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Map<String, Object> getInputSchema() { return inputSchema; }
    public void setInputSchema(Map<String, Object> inputSchema) { this.inputSchema = inputSchema; }
}

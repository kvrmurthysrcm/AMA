package com.ama.mcptools.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

/**
 * Generic request used by the Agent/Client to invoke a tool.
 *
 * name      -> tool name such as get_top_products
 * arguments -> tool-specific payload that will be forwarded to analytics service
 */
public class ToolCallRequest {
    @NotBlank
    private String name;

    private Map<String, Object> arguments;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Map<String, Object> getArguments() { return arguments; }
    public void setArguments(Map<String, Object> arguments) { this.arguments = arguments; }
}

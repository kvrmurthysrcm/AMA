package com.ama.mcptools.dto;

import java.util.Map;

/**
 * Standard tool-call response wrapper.
 *
 * This keeps the response model Agent-friendly:
 * - tool metadata
 * - raw downstream response
 * - pass-through status
 */
public class ToolCallResponse {
    private String toolName;
    private String endpointPath;
    private int statusCode;
    private Object data;
    private Map<String, Object> meta;

    public ToolCallResponse() {}

    public ToolCallResponse(String toolName, String endpointPath, int statusCode, Object data, Map<String, Object> meta) {
        this.toolName = toolName;
        this.endpointPath = endpointPath;
        this.statusCode = statusCode;
        this.data = data;
        this.meta = meta;
    }

    public String getToolName() { return toolName; }
    public void setToolName(String toolName) { this.toolName = toolName; }

    public String getEndpointPath() { return endpointPath; }
    public void setEndpointPath(String endpointPath) { this.endpointPath = endpointPath; }

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public Map<String, Object> getMeta() { return meta; }
    public void setMeta(Map<String, Object> meta) { this.meta = meta; }
}

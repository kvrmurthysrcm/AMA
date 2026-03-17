package com.ama.mcptools.service;

import com.ama.mcptools.dto.ToolCallRequest;
import com.ama.mcptools.dto.ToolCallResponse;
import com.ama.mcptools.model.ToolDefinition;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Main execution service for MCP-style tool calls.
 *
 * Responsibilities:
 * 1. Resolve the tool from the catalog
 * 2. Forward arguments to the mapped analytics endpoint
 * 3. Wrap the downstream response in a stable tool-response envelope
 */
@Service
public class ToolExecutionService {

    private final ToolCatalogService toolCatalogService;
    private final AnalyticsProxyClient analyticsProxyClient;

    public ToolExecutionService(ToolCatalogService toolCatalogService, AnalyticsProxyClient analyticsProxyClient) {
        this.toolCatalogService = toolCatalogService;
        this.analyticsProxyClient = analyticsProxyClient;
    }

    public ToolCallResponse execute(ToolCallRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Tool name is required");
        }

        ToolDefinition tool = toolCatalogService.findEnabledTool(request.getName())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unknown or disabled tool: " + request.getName()));

        Map<String, Object> arguments = request.getArguments() == null ? Map.of() : request.getArguments();

        ResponseEntity<Object> downstream = analyticsProxyClient.post(tool.getEndpointPath(), arguments);

        Map<String, Object> meta = new HashMap<>();
        meta.put("preferredChart", tool.getPreferredChart());
        meta.put("forwardedTo", tool.getEndpointPath());

        return new ToolCallResponse(
                tool.getName(),
                tool.getEndpointPath(),
                downstream.getStatusCode().value(),
                downstream.getBody(),
                meta
        );
    }
}

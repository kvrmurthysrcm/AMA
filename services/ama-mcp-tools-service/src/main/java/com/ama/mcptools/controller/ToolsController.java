package com.ama.mcptools.controller;

import com.ama.mcptools.dto.ToolCallRequest;
import com.ama.mcptools.dto.ToolCallResponse;
import com.ama.mcptools.model.ToolDefinition;
import com.ama.mcptools.service.ToolCatalogService;
import com.ama.mcptools.service.ToolExecutionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for the MCP-style tool service.
 *
 * These are deliberately simple and easy to test from Postman:
 * - GET  /api/mcp/v1/tools/list
 * - POST /api/mcp/v1/tools/call
 *
 * Later, if you want a formal JSON-RPC MCP server, this module can be evolved further.
 */
@RestController
@RequestMapping("/api/mcp/v1/tools")
public class ToolsController {

    private final ToolCatalogService toolCatalogService;
    private final ToolExecutionService toolExecutionService;

    public ToolsController(ToolCatalogService toolCatalogService, ToolExecutionService toolExecutionService) {
        this.toolCatalogService = toolCatalogService;
        this.toolExecutionService = toolExecutionService;
    }

    @GetMapping("/list")
    public List<ToolDefinition> listTools() {
        return toolCatalogService.listEnabledTools();
    }

    @PostMapping("/call")
    public ToolCallResponse callTool(@Valid @RequestBody ToolCallRequest request) {
        return toolExecutionService.execute(request);
    }
}

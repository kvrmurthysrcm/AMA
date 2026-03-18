package com.ama.agent.client;

import com.ama.agent.model.ToolDefinition;

import java.util.List;
import java.util.Map;

public interface ToolClient {
    List<ToolDefinition> listTools();
    Object callTool(String name, Map<String, Object> arguments);
}

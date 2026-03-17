package com.ama.mcptools.service;

import com.ama.mcptools.config.ToolCatalogLoader;
import com.ama.mcptools.model.ToolDefinition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Read-only service around the loaded tool catalog.
 */
@Service
public class ToolCatalogService {

    private final ToolCatalogLoader loader;

    public ToolCatalogService(ToolCatalogLoader loader) {
        this.loader = loader;
    }

    public List<ToolDefinition> listEnabledTools() {
        return loader.getTools().stream().filter(ToolDefinition::isEnabled).toList();
    }

    public Optional<ToolDefinition> findEnabledTool(String name) {
        return loader.getTools().stream()
                .filter(ToolDefinition::isEnabled)
                .filter(t -> t.getName().equals(name))
                .findFirst();
    }
}

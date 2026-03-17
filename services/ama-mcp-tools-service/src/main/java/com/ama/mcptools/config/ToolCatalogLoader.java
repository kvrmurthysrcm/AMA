package com.ama.mcptools.config;

import com.ama.mcptools.model.ToolDefinition;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Loads tool definitions from tool-catalog.yaml.
 *
 * Keeping the tool catalog in YAML makes the service easier to evolve:
 * - add a new tool
 * - disable a tool
 * - change descriptions
 * without touching routing logic.
 */
@Component
public class ToolCatalogLoader {

    private final List<ToolDefinition> tools;

    public ToolCatalogLoader() {
        this.tools = loadTools();
    }

    public List<ToolDefinition> getTools() {
        return tools;
    }

    private List<ToolDefinition> loadTools() {
        try (InputStream in = new ClassPathResource("tool-catalog.yaml").getInputStream()) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(in, new TypeReference<List<ToolDefinition>>() {});
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to load tool-catalog.yaml", ex);
        }
    }
}

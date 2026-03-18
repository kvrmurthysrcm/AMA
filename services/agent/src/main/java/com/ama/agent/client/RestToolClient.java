package com.ama.agent.client;

import com.ama.agent.config.AgentProperties;
import com.ama.agent.dto.ToolCallRequest;
import com.ama.agent.model.ToolDefinition;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class RestToolClient implements ToolClient {

    private final RestClient restClient;
    private final AgentProperties properties;

    public RestToolClient(AgentProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.builder()
                .baseUrl(properties.getTools().getBaseUrl())
                .build();
    }

    @Override
    public List<ToolDefinition> listTools() {
        return restClient.get()
                .uri(properties.getTools().getListPath())
                .retrieve()
                .body(new ParameterizedTypeReference<List<ToolDefinition>>() {});
    }

    @Override
    public Object callTool(String name, Map<String, Object> arguments) {
        return restClient.post()
                .uri(properties.getTools().getCallPath())
                .body(new ToolCallRequest(name, arguments))
                .retrieve()
                .body(Object.class);
    }
}

package com.ama.agent.service;

import com.ama.agent.client.LlmClient;
import com.ama.agent.client.ToolClient;
import com.ama.agent.dto.AgentQueryRequest;
import com.ama.agent.dto.AgentQueryResponse;
import com.ama.agent.model.ToolDefinition;
import com.ama.agent.model.ToolSelectionResult;
import com.ama.agent.util.JsonExtractionUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentOrchestratorService {

    private final ToolClient toolClient;
    private final LlmClient llmClient;
    private final PromptBuilderService promptBuilderService;

    public AgentOrchestratorService(ToolClient toolClient, LlmClient llmClient, PromptBuilderService promptBuilderService) {
        this.toolClient = toolClient;
        this.llmClient = llmClient;
        this.promptBuilderService = promptBuilderService;
    }

    public AgentQueryResponse handle(AgentQueryRequest request) {
        List<ToolDefinition> tools = toolClient.listTools();

        String selectionSystem = promptBuilderService.buildToolSelectionSystemPrompt(tools);
        String selectionUser = promptBuilderService.buildToolSelectionUserPrompt(request);

        String selectionRaw = llmClient.generate(selectionSystem, selectionUser);
        ToolSelectionResult selection = JsonExtractionUtil.parseJsonObject(selectionRaw, ToolSelectionResult.class);

        if (selection.getSelectedTool() == null || selection.getSelectedTool().isBlank()) {
            throw new IllegalStateException("LLM did not return selectedTool");
        }
        if (selection.getToolArguments() == null) {
            throw new IllegalStateException("LLM did not return toolArguments");
        }

        selection.getToolArguments().putIfAbsent("tenantId", request.getTenantId());
        if (request.getAccountIds() != null && !selection.getToolArguments().containsKey("accountIds")) {
            selection.getToolArguments().put("accountIds", request.getAccountIds());
        }
        if (request.getMarketplaceCodes() != null && !selection.getToolArguments().containsKey("marketplaceCodes")) {
            selection.getToolArguments().put("marketplaceCodes", request.getMarketplaceCodes());
        }

        Object toolResult = toolClient.callTool(selection.getSelectedTool(), selection.getToolArguments());

        String summarySystem = promptBuilderService.buildSummarySystemPrompt();
        String summaryUser = promptBuilderService.buildSummaryUserPrompt(
                request.getQuestion(),
                selection.getSelectedTool(),
                toolResult
        );

        String summary = llmClient.generate(summarySystem, summaryUser);

        return new AgentQueryResponse(
                selection.getSelectedTool(),
                selection.getToolArguments(),
                summary,
                toolResult,
                selection.getChartType()
        );
    }
}

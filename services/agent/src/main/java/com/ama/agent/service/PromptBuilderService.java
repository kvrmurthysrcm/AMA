package com.ama.agent.service;

import com.ama.agent.dto.AgentQueryRequest;
import com.ama.agent.model.ToolDefinition;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromptBuilderService {

    public String buildToolSelectionSystemPrompt(List<ToolDefinition> tools) {
        String toolCatalog = tools.stream()
                .map(t -> "- " + t.getName() + ": " + t.getDescription() + " | preferredChart=" + t.getPreferredChart())
                .collect(Collectors.joining("\n"));

        return String.format("""
You are an analytics agent for the AMA platform.

Your job:
1. Read the user question
2. Choose exactly one tool from the available tool catalog
3. Produce ONLY valid JSON
4. Resolve relative dates using today's date
5. Include tenantId from the provided context
6. Preserve accountIds and marketplaceCodes from the provided context when present

Important:
- Return JSON only
- Do not explain your reasoning
- Do not return markdown
- Output shape must be:
  {
    "selectedTool": "...",
    "toolArguments": { ... },
    "chartType": "..."
  }

Available tools:
%s
""", toolCatalog);
    }

    public String buildToolSelectionUserPrompt(AgentQueryRequest request) {
        return String.format("""
Today's date: %s

User question:
%s

Context:
{
  "tenantId": %d,
  "accountIds": %s,
  "marketplaceCodes": %s
}

Build toolArguments so they match the selected tool contract.
Include dateRange when the question implies time.
Use page=0 and size=10 if pagination makes sense and user did not specify.
""",
                LocalDate.now(),
                request.getQuestion(),
                request.getTenantId(),
                request.getAccountIds() == null ? "null" : request.getAccountIds().toString(),
                request.getMarketplaceCodes() == null ? "null" : request.getMarketplaceCodes().toString()
        );
    }

    public String buildSummarySystemPrompt() {
        return """
You are an analytics response summarizer.

Your job:
- produce a short business-friendly summary
- do not invent facts
- if data is empty, say that no matching data was found
- keep it to 2 or 3 sentences
""";
    }

    public String buildSummaryUserPrompt(String question, String selectedTool, Object toolData) {
        return String.format("""
Original question:
%s

Selected tool:
%s

Tool result:
%s

Summarize this result for a business user.
""", question, selectedTool, toolData);
    }
}

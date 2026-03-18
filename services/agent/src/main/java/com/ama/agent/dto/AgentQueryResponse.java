package com.ama.agent.dto;

import java.util.Map;

public class AgentQueryResponse {
    private String selectedTool;
    private Map<String, Object> toolArguments;
    private String summary;
    private Object data;
    private String chartType;

    public AgentQueryResponse() {}

    public AgentQueryResponse(String selectedTool, Map<String, Object> toolArguments, String summary, Object data, String chartType) {
        this.selectedTool = selectedTool;
        this.toolArguments = toolArguments;
        this.summary = summary;
        this.data = data;
        this.chartType = chartType;
    }

    public String getSelectedTool() { return selectedTool; }
    public void setSelectedTool(String selectedTool) { this.selectedTool = selectedTool; }

    public Map<String, Object> getToolArguments() { return toolArguments; }
    public void setToolArguments(Map<String, Object> toolArguments) { this.toolArguments = toolArguments; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public String getChartType() { return chartType; }
    public void setChartType(String chartType) { this.chartType = chartType; }
}

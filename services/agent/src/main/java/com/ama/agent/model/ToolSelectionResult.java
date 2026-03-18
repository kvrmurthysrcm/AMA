package com.ama.agent.model;

import java.util.Map;

public class ToolSelectionResult {
    private String selectedTool;
    private Map<String, Object> toolArguments;
    private String chartType;

    public String getSelectedTool() { return selectedTool; }
    public void setSelectedTool(String selectedTool) { this.selectedTool = selectedTool; }

    public Map<String, Object> getToolArguments() { return toolArguments; }
    public void setToolArguments(Map<String, Object> toolArguments) { this.toolArguments = toolArguments; }

    public String getChartType() { return chartType; }
    public void setChartType(String chartType) { this.chartType = chartType; }
}

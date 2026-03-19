package com.ama.agent.controller;

import com.ama.agent.dto.AgentQueryRequest;
import com.ama.agent.dto.AgentQueryResponse;
import com.ama.agent.service.AgentOrchestratorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agent/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class AgentController {

    private final AgentOrchestratorService agentOrchestratorService;

    public AgentController(AgentOrchestratorService agentOrchestratorService) {
        this.agentOrchestratorService = agentOrchestratorService;
    }

    @PostMapping("/query")
    public AgentQueryResponse query(@Valid @RequestBody AgentQueryRequest request) {
        return agentOrchestratorService.handle(request);
    }
}

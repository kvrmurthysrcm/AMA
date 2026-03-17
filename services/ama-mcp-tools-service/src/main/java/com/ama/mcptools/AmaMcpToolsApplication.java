package com.ama.mcptools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the MCP Tool service.
 *
 * This is intentionally a separate Spring Boot service so you can keep
 * Analytics business logic independent from the Agent/MCP contract layer.
 */
@SpringBootApplication
public class AmaMcpToolsApplication {
    public static void main(String[] args) {
        SpringApplication.run(AmaMcpToolsApplication.class, args);
    }
}

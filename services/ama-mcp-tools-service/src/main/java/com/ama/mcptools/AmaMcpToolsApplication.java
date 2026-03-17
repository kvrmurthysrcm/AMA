package com.ama.mcptools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Entry point for the MCP Tool service.
 *
 * This service does not need a database.
 * It only exposes tool metadata and forwards tool calls to the analytics service.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AmaMcpToolsApplication {
    public static void main(String[] args) {
        SpringApplication.run(AmaMcpToolsApplication.class, args);
    }
}
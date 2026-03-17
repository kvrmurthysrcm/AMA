# AMA MCP Tools Service

This module exposes your existing Analytics service as a set of MCP-style tools.

## What this service does

- Lists available tools at `GET /api/mcp/v1/tools/list`
- Executes tools at `POST /api/mcp/v1/tools/call`
- Internally maps each tool to an Analytics service endpoint
- Forwards the request payload to the Analytics service and returns the response

## Why this is useful

Your Agent/LLM should call tools by stable names such as:

- `get_dashboard_summary`
- `get_sales_timeseries`
- `get_top_products`
- `get_stockout_risk`

This service makes those names discoverable and executable, while your existing
analytics service continues to own the actual business logic and database queries.

## Run configuration

By default the service expects the analytics service to be available at:

`http://localhost:8081`

You can change that in `src/main/resources/application.yml`.

## Endpoints

### 1. List tools
`GET /api/mcp/v1/tools/list`

### 2. Call a tool
`POST /api/mcp/v1/tools/call`

Example body:

```json
{
  "name": "get_top_products",
  "arguments": {
    "tenantId": 1001,
    "accountIds": [1001],
    "marketplaceCodes": ["IN"],
    "dateRange": {
      "startDate": "2025-03-20",
      "endDate": "2025-06-30"
    },
    "pagination": {
      "page": 0,
      "size": 20
    },
    "sort": {
      "field": "netSales",
      "direction": "DESC"
    }
  }
}
```

## How to integrate into your root project

### settings.gradle
Add:

```gradle
include 'services:ama-mcp-tools-service'
project(':services:ama-mcp-tools-service').projectDir = file('services/ama-mcp-tools-service')
```

### Run from root
```bash
./gradlew :services:ama-mcp-tools-service:bootRun
```
or on Windows:
```bat
gradlew :services:ama-mcp-tools-service:bootRun
```

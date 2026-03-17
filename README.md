# AMA Platform

Gradle multi-module Java 21 / Spring Boot 3.4 project for the Amazon Marketplace Analytics platform.

## Modules
- `services/common` - shared API wrapper, exceptions, base DTOs
- `services/platform` - trace-id filter, global exception handling, AOP logging, OpenAPI config
- `services/analytics` - MVP-1 analytics endpoints
- `services/tools` - placeholder module
- `services/mcp-server` - placeholder module
- `services/agent` - placeholder module

## Build & Run modules 
.\gradlew.bat  :services:analytics:clean build
.\gradlew.bat  :services:analytics:bootRun

.\gradlew.bat  :services:tools:clean build
.\gradlew.bat  :services:tools:bootRun

.\gradlew :services:ama-mcp-tools-service:bootRun

.\gradlew.bat  :services:mcp-server:clean build
.\gradlew.bat  :services:mcp-server:bootRun

.\gradlew.bat  :services:agent:clean build
.\gradlew.bat  :services:agent:bootRun

.\gradlew.bat  :services:common:clean build
.\gradlew.bat  :services:common:bootRun

.\gradlew.bat  :services:platform:clean build
.\gradlew.bat  :services:platform:bootRun

## MVP-1 analytics endpoints
All analytics APIs are `POST`.

- `/api/analytics/v1/dashboard/summary`
- `/api/analytics/v1/dashboard/trends`
- `/api/analytics/v1/sales/timeseries`
- `/api/analytics/v1/sales/by-product`
- `/api/analytics/v1/traffic/conversion`
- `/api/analytics/v1/ads/overview`
- `/api/analytics/v1/ads/by-product`
- `/api/analytics/v1/inventory/stockout-risk`
- `/api/analytics/v1/finance/profitability`
- `/api/analytics/v1/products/performance`
- `/api/analytics/v1/insights/summary`
- `/api/analytics/v1/metadata/accounts`
- `/api/analytics/v1/metadata/brands`
- `/api/analytics/v1/metadata/categories`
- `/api/analytics/v1/query`

## Notes
- Configure PostgreSQL datasource properties in `services/analytics/src/main/resources/application.yml`.

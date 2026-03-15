# AMA Platform

Gradle multi-module Java 21 / Spring Boot 3.4 project for the Amazon Marketplace Analytics platform.

## Modules
- `services/common` - shared API wrapper, exceptions, base DTOs
- `services/platform` - trace-id filter, global exception handling, AOP logging, OpenAPI config
- `services/analytics` - MVP-1 analytics endpoints
- `services/tools` - placeholder module
- `services/mcp-server` - placeholder module
- `services/agent` - placeholder module

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
- Gradle wrapper is not included in this zip because the runtime environment used for generation did not have Gradle installed.
- Import the project into IntelliJ as a Gradle project using the included `settings.gradle` and `build.gradle` files.
- Configure PostgreSQL datasource properties in `services/analytics/src/main/resources/application.yml`.

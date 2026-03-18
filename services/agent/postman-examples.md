# Quick test

POST http://localhost:8083/api/agent/v1/query

```json
{
  "question": "Which products sold best in the last month?",
  "tenantId": 1001,
  "accountIds": [1001],
  "marketplaceCodes": ["IN"]
}
```

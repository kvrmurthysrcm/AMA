# AMA Agent Service

This service accepts natural-language analytics questions, discovers tools from the Tools service,
asks an LLM to choose the best tool + arguments, invokes the selected tool, and optionally asks
the LLM to produce a short business summary.

## Current flow

User question
-> Agent Service
-> GET Tools Service /api/mcp/v1/tools/list
-> LLM chooses { selectedTool, toolArguments, chartType }
-> POST Tools Service /api/mcp/v1/tools/call
-> LLM summarizes result
-> Agent returns:
   - selectedTool
   - toolArguments
   - summary
   - data
   - chartType

## Run

1. Start Analytics service on 8081
2. Start Tools service on 8082
3. Start Agent service on 8083

```bat
gradlew :services:agent:bootRun
```

## Example request

POST http://localhost:8083/api/agent/v1/query

```json
{
  "question": "Which products sold best in the last month?",
  "tenantId": 1001,
  "accountIds": [1001],
  "marketplaceCodes": ["IN"]
}
```


## Configuration

Use root-level `llm` properties in `application.yml` or `application.properties`:

```yaml
llm:
  provider: openai
  temperature: 0.2
  maxTokens: 500
```

`provider` defaults to `openai`. Supported values: `openai`, `azureOpenai`, `cohere`, `ollama`.


$env:OPENAI_API_KEY="sk-your-api-key-here" -- for the session

setx OPENAI_API_KEY "sk-your-api-key-here" -- permanently
echo $env:OPENAI_API_KEY -- to test the variable value
./gradlew :services:agent:bootRun



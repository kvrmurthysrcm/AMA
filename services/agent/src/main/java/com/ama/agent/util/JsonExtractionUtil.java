package com.ama.agent.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonExtractionUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonExtractionUtil() {}

    public static <T> T parseJsonObject(String text, Class<T> clazz) {
        try {
            String trimmed = text.trim();
            if (trimmed.startsWith("```")) {
                trimmed = trimmed.replaceAll("^```(?:json)?\\s*", "");
                trimmed = trimmed.replaceAll("\\s*```$", "");
            }

            int start = trimmed.indexOf('{');
            int end = trimmed.lastIndexOf('}');
            if (start >= 0 && end > start) {
                trimmed = trimmed.substring(start, end + 1);
            }

            return OBJECT_MAPPER.readValue(trimmed, clazz);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to parse LLM JSON response: " + text, ex);
        }
    }
}

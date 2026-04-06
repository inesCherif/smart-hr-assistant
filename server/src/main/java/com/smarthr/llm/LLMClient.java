package com.smarthr.llm;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smarthr.config.EnvConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Handles communication with the LLM API safely and modularly.
 * Built to be configurable to easily swap out models/endpoints using .env
 */
public class LLMClient {
    private final String apiKey;
    private final String apiUrl;
    private final String model;
    private final HttpClient httpClient;

    public LLMClient() {
        this.apiKey = EnvConfig.get("GROQ_API_KEY");
        // Default to Groq's openAI compatible endpoint if not specified
        this.apiUrl = EnvConfig.get("GROQ_API_URL", "https://api.groq.com/openai/v1/chat/completions");
        this.model = EnvConfig.get("GROQ_MODEL", "gpt-oss-20b"); 
        
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            System.err.println("Warning: GROQ_API_KEY is missing from environment variables or .env file!");
        }

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    public String generateSQL(String systemPrompt, String userQuestion) {
        try {
            // Build the JSON payload to OpenAI API compatibility standards
            JsonObject payload = new JsonObject();
            payload.addProperty("model", this.model);
            payload.addProperty("temperature", 0.0); // 0 ensures deterministic SQL outputs
            
            JsonArray messages = new JsonArray();
            
            JsonObject systemMsg = new JsonObject();
            systemMsg.addProperty("role", "system");
            systemMsg.addProperty("content", systemPrompt);
            messages.add(systemMsg);
            
            JsonObject userMsg = new JsonObject();
            userMsg.addProperty("role", "user");
            userMsg.addProperty("content", userQuestion);
            messages.add(userMsg);
            
            payload.add("messages", messages);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.apiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + this.apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("API Error: HTTP " + response.statusCode());
                System.err.println("Response: " + response.body());
                return null;
            }

            // Parse response safely
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            return jsonResponse.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString()
                    .trim();

        } catch (Exception e) {
            System.err.println("Error communicating with LLM API: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

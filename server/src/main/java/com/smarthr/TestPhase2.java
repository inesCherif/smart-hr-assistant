package com.smarthr;

import com.smarthr.database.DatabaseManager;
import com.smarthr.llm.LLMClient;

import java.sql.Connection;

/**
 * A testing class to strictly verify Phase 2 server foundations (Database and LLM)
 * before moving on to Phase 3 (RMI Server).
 */
public class TestPhase2 {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   Testing Phase 2: Server Foundations ");
        System.out.println("=================================================\n");
        
        // 1. Test Database
        System.out.println("[1/2] Testing Database Connection...");
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn != null) {
            System.out.println("Database connection test passed.\n");
        } else {
            System.out.println("Database connection test failed.\n");
        }

        // 2. Test LLM Client
        System.out.println("[2/2] Testing LLM Client Connection...");
        LLMClient llmClient = new LLMClient();
        
        String systemPrompt = "You are an SQL expert. Return only the SQL query, no markdown.";
        String question = "Give me a query to select all employees.";
        
        System.out.println("   Sending test Prompt to LLM: \"" + question + "\"");
        
        String sqlResponse = llmClient.generateSQL(systemPrompt, question);
        if (sqlResponse != null && !sqlResponse.isEmpty()) {
            System.out.println("\n LLM Responded Successfully!");
            System.out.println("   Result Output:\n   " + sqlResponse);
        } else {
            System.out.println("\n  LLM Query failed. Check your API Key and Network.");
        }
        
        System.out.println("\n=================================================");
        System.out.println("   Phase 2 Test Complete.");
        System.out.println("=================================================");
    }
}

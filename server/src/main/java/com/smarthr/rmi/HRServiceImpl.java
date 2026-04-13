package com.smarthr.rmi;

import com.smarthr.database.DatabaseManager;
import com.smarthr.llm.LLMClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class HRServiceImpl extends UnicastRemoteObject implements HRService {

    private final LLMClient llmClient;
    
    // Schema required to tightly instruct the AI
    private static final String SCHEMA_PROMPT = 
            "You are an SQL assistant for an HR MySQL database. " +
            "Your job is to read the user's natural language question and convert it strictly into a standard MySQL query. " +
            "Do NOT add any explanation, markdown, backticks, or comments. Output ONLY the raw SQL query string.\n" +
            "Here is the database schema:\n" +
            "- employees (id, name, department, hire_date, salary, email)\n" +
            "- departments (id, name, manager_name, budget)\n" +
            "- vacations (id, employee_id, start_date, end_date, days_taken)\n" +
            "- absences (id, employee_id, date, reason, justified)\n";

    public HRServiceImpl() throws RemoteException {
        super();
        this.llmClient = new LLMClient();
    }

    @Override
    public String askQuestion(String question) throws RemoteException {
        System.out.println("\n[📬 Received Question]: " + question);
        
        try {
            // 1. Convert natural language question to SQL using the configured LLM API
            String generatedSql = llmClient.generateSQL(SCHEMA_PROMPT, question);
            
            if (generatedSql == null || generatedSql.isEmpty()) {
                return "Error: Could not generate SQL from the AI model. Check LLM logs.";
            }

            // Fallback safety: Clean up backticks purely in case the AI ignored the "no markdown" rule
            generatedSql = generatedSql.replaceAll("```sql", "").replaceAll("```", "").trim();
            System.out.println("[🧠 LLM Generated SQL]: " + generatedSql);

            // 2. Obtain database connection and verify it
            Connection dbConn = DatabaseManager.getInstance().getConnection();
            if (dbConn == null) {
                return "Error: Database connection is unavailable.";
            }

            // Strict security check ensuring only read operations happen
            if (!generatedSql.toLowerCase().startsWith("select")) {
                return "Security Error: Attempted a non-SELECT statement. (" + generatedSql + ")";
            }

            // 3. Execute query and parse standard JDBC results gracefully
            try (Statement stmt = dbConn.createStatement();
                 ResultSet rs = stmt.executeQuery(generatedSql)) {
                 
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Format the results generically for any dynamic set of columns returned
                StringBuilder answer = new StringBuilder();
                int rowCount = 0;
                
                while (rs.next()) {
                    rowCount++;
                    for (int i = 1; i <= columnCount; i++) {
                        answer.append(metaData.getColumnLabel(i)).append(": ").append(rs.getString(i));
                        if (i < columnCount) answer.append(" | ");
                    }
                    answer.append("\n");
                }
                
                if (rowCount == 0) {
                    return "Database Query executed:\n> " + generatedSql + "\n\nResult:\nNo records found matching the criteria.";
                }

                return "Database Query executed:\n> " + generatedSql + "\n\nResult:\n" + answer.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Internal Server Error occurred while processing your request: " + e.getMessage();
        }
    }
}

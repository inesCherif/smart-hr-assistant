package com.smarthr.client;

import com.smarthr.rmi.HRService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class HRClient {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("       🧠 Smart HR Assistant Interface 🧠       ");
        System.out.println("=================================================");

        // Fetch location properly; allowing defaults for local testing 
        // but completely reconfigurable safely without hardcoding when deployed in Docker
        String host = System.getenv().getOrDefault("RMI_HOST", "localhost");
        String portStr = System.getenv().getOrDefault("RMI_PORT", "1099");
        int port = 1099;
        
        try {
            port = Integer.parseInt(portStr);
            System.out.println("🔄 Looking for the RMI Server Registry at " + host + ":" + port + "...");
            
            // Connect to Registry and fetch Service Object using interface
            Registry registry = LocateRegistry.getRegistry(host, port);
            HRService smartHR = (HRService) registry.lookup("SmartHRService");
            
            System.out.println("✅ Connected successfully to the Server Core!\n");

            Scanner scanner = new Scanner(System.in);
            System.out.println("💡 Type your HR question in natural language.");
            System.out.println("   Example: 'How many employees work in IT?'");
            System.out.println("   (Type 'exit' to quit)\n");

            while (true) {
                System.out.print("HR Manager > ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                    System.out.println("👋 Goodbye!");
                    break;
                }

                if (input.isEmpty()) {
                    continue;
                }

                System.out.println("⏳ Analyzing and fetching data...");
                try {
                    // This invokes the RMI network call to the Java Server remotely
                    String answer = smartHR.askQuestion(input);
                    
                    System.out.println("\n-------------------------------------------------");
                    System.out.println(answer);
                    System.out.println("-------------------------------------------------\n");
                } catch (Exception e) {
                    System.err.println("❌ Network Error: The server appears to be unreachable or threw an error.");
                    System.err.println("Details: " + e.getMessage());
                }
            }
            scanner.close();
            
        } catch (Exception e) {
            System.err.println("❌ Fatal Client Error: Could not connect to RMI service.");
            e.printStackTrace();
        }
    }
}

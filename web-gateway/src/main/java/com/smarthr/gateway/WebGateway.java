package com.smarthr.gateway;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.smarthr.rmi.HRService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WebGateway {
    private static HRService smartHR;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        String host = System.getenv().getOrDefault("RMI_HOST", "server");
        int port = Integer.parseInt(System.getenv().getOrDefault("RMI_PORT", "1099"));
        
        System.out.println("⏳ Connecting to RMI Server at " + host + ":" + port);
        try {
            // Attempt RMI bind first
            Registry registry = LocateRegistry.getRegistry(host, port);
            smartHR = (HRService) registry.lookup("SmartHRService");
            System.out.println("✅ Linked securely to Core HR Server!");

            // Boot Lightweight Native Java HTTP Server on 8080
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/api/chat", new ChatHandler());
            server.setExecutor(null); 
            server.start();
            System.out.println("🚀 Web Gateway listening for React requests on port 8080...");
        } catch (Exception e) {
            System.err.println("❌ Web Gateway Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static class ChatHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Implement strict Cross Origin handling allowing any browser to safely fetch
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            // Browsers securely preflight API requests using OPTIONS
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                try (InputStream is = exchange.getRequestBody()) {
                    String body = new String(is.readAllBytes());
                    // Extract exactly what the user typed in the browser
                    JsonObject jsonReq = gson.fromJson(body, JsonObject.class);
                    String question = jsonReq.get("question").getAsString();
                    
                    System.out.println("🌐 Web Request: " + question);
                    
                    // Route over secure RMI interface directly into business logic container
                    String answer = smartHR.askQuestion(question);

                    // Send the answer cleanly wrapped inside JSON back to React
                    JsonObject jsonResponse = new JsonObject();
                    jsonResponse.addProperty("answer", answer);
                    
                    String resString = gson.toJson(jsonResponse);
                    byte[] resBytes = resString.getBytes();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, resBytes.length);
                    
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(resBytes);
                    }
                } catch (Exception e) {
                    JsonObject errorResponse = new JsonObject();
                    errorResponse.addProperty("error", "Gateway Routing Error: " + e.getMessage());
                    byte[] resBytes = gson.toJson(errorResponse).getBytes();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(500, resBytes.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(resBytes);
                    }
                }
            } else {
                exchange.sendResponseHeaders(405, -1); 
            }
        }
    }
}

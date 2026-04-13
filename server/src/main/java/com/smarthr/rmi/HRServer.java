package com.smarthr.rmi;

import com.smarthr.config.EnvConfig;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HRServer {
    public static void main(String[] args) {
        try {
            System.out.println("=================================================");
            System.out.println("       🚀 Starting Smart HR RMI Server 🚀       ");
            System.out.println("=================================================");

            String registryHost = EnvConfig.get("RMI_HOST", "localhost");
            int port = Integer.parseInt(EnvConfig.get("RMI_PORT", "1099"));
            
            // Set hostname safely allowing container routing later or localhost dev
            System.setProperty("java.rmi.server.hostname", registryHost);

            Registry registry;
            try {
                // First, try utilizing an existing isolated Registry (for Docker modular setups)
                registry = LocateRegistry.getRegistry(registryHost, port);
                registry.list(); 
                System.out.println("✅ Found an independently running RMI Registry at " + registryHost + ":" + port);
            } catch (Exception e) {
                // Graceful fallback to creating it directly inside this JVM if testing purely locally
                System.out.println("⚠️ No external registry found. Creating integrated local registry on port " + port);
                registry = LocateRegistry.createRegistry(port);
            }

            // Expose the implementation via the registry map
            HRService targetService = new HRServiceImpl();
            registry.rebind("SmartHRService", targetService);

            System.out.println("✅ Smart HR Server is successfully bound inside the Registry!");
            System.out.println("⏳ Awaiting internal client connections...");

            // Keep the application running indefinitely
            while (true) {
                Thread.sleep(Long.MAX_VALUE);
            }

        } catch (Exception e) {
            System.err.println("❌ Smart HR Server encountered a fatal initialization error:");
            e.printStackTrace();
        }
    }
}

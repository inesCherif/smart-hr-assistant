package com.smarthr.rmi;

import com.smarthr.config.EnvConfig;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HRServer {
    public static void main(String[] args) {
        try {
            System.out.println("=================================================");
            System.out.println("       Starting Smart HR RMI Server       ");
            System.out.println("=================================================");

            String registryHost = EnvConfig.get("RMI_HOST", "localhost");
            String serverHost = EnvConfig.get("SERVER_HOST", "localhost");
            int port = Integer.parseInt(EnvConfig.get("RMI_PORT", "1099"));
            
            // Set hostname safely allowing container routing later or localhost dev
            System.setProperty("java.rmi.server.hostname", serverHost);

            HRService targetService = new HRServiceImpl();
            Registry registry;
            try {
                registry = LocateRegistry.getRegistry(registryHost, port);
                // RMI strictly isolates rebinds to localhost. If cross-container, this throws ServerException.
                registry.rebind("SmartHRService", targetService);
                System.out.println("Successfully bound to external RMI Registry at " + registryHost + ":" + port);
            } catch (Exception e) {
                System.out.println("External registry rejected bind (RMI security rules). Creating integrated local registry on port " + port);
                registry = LocateRegistry.createRegistry(port);
                registry.rebind("SmartHRService", targetService);
            }

            System.out.println("Smart HR Server is successfully bound inside the Registry!");
            System.out.println("Awaiting internal client connections...");

            // Keep the application running indefinitely
            while (true) {
                Thread.sleep(Long.MAX_VALUE);
            }

        } catch (Exception e) {
            System.err.println("Smart HR Server encountered a fatal initialization error:");
            e.printStackTrace();
        }
    }
}

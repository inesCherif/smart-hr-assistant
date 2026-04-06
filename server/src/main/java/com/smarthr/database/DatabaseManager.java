package com.smarthr.database;

import com.smarthr.config.EnvConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton Database Manager to securely and efficiently handle connections.
 **/
public class DatabaseManager {
    
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        connect();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void connect() {
        String url = EnvConfig.get("DB_URL", "jdbc:mysql://localhost:3306/hr_db");
        String user = EnvConfig.get("DB_USER", "root");
        String password = EnvConfig.get("DB_PASSWORD", "root");

        try {
            // Check if driver class exists
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL Database successfully!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status: " + e.getMessage());
        }
        return connection;
    }
}

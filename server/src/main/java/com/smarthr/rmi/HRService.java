package com.smarthr.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The RMI remote interface. This represents the contract 
 * between the HR client and the central RMI Server.
 */
public interface HRService extends Remote {
    /**
     * Accepts a natural language question from the HR user,
     * processes it via LLM to SQL, runs it, and returns the result.
     *
     * @param question the user's natural language question
     * @return a formatted readable string indicating the outcome and SQL query
     * @throws RemoteException if the RMI call fails
     */
    String askQuestion(String question) throws RemoteException;
}

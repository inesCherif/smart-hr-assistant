package com.smarthr.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The EXACT SAME RMI remote interface as the server.
 * Both client and server must share this identical contract to communicate.
 */
public interface HRService extends Remote {
    
    /**
     * Sends the HR question to the backend.
     * @param question Natural language question
     * @return Formatted AI/SQL answer
     * @throws RemoteException
     */
    String askQuestion(String question) throws RemoteException;
}

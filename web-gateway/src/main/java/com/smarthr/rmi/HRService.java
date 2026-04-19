package com.smarthr.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The exact RMI contract needed by the Web Gateway to talk to the core server seamlessly.
 */
public interface HRService extends Remote {
    String askQuestion(String question) throws RemoteException;
}

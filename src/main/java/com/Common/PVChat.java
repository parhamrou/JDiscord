package com.Common;

import com.Common.*;
import com.Server.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class PVChat extends TextChat implements Serializable, Runnable {

    private final User user1;
    private final User user2;
    private boolean isUser1Blocked = false;
    private boolean isUser2Blocked = false;

    
    // constructor
    public PVChat(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    @Override
    public void run() {
        try {
            chatHandlers = new HashMap<>();
            Random random = new Random();
            serverSocket = new ServerSocket(0);
            DataManager.addServerSocket(serverSocket);
            setPortNumber(serverSocket.getLocalPort());
            System.out.println("Now the PVChat is listening and waiting for clients in port " + getPortNumber());
            Socket socket;
            int handler_ID;
            while (true) {
                socket = serverSocket.accept();
                System.out.println("A user is entered!");
                handler_ID = random.nextInt();
                PVHandler pvHandler = new PVHandler(this, socket, handler_ID);
                chatHandlers.put(handler_ID, pvHandler);
                new Thread(pvHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isBlocked(String username) {
        if (user1.getUsername().equalsIgnoreCase(username) && isUser1Blocked) {
            return true;
        } else if (user2.getUsername().equalsIgnoreCase(username) && isUser2Blocked) {
            return true;
        } else {
            return false;
        }
    }


    public User getUser1() {
        return user1;
    }


    public User getUser2() {
        return user2;
    }


    public void block(String username) {
        if (user1.getUsername().equalsIgnoreCase(username)) {
            isUser2Blocked = true;
        } else {
            isUser1Blocked = true;
        }
    }
}

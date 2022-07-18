package com.Server;

import com.Common.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class TextChannel extends TextChat implements Runnable, Serializable{

    private final String channelName;
    private final Server parentServer;

    public TextChannel(String channelName, Server server) {
        this.channelName = channelName;
        this.parentServer = server;
    }


    @Override
    public void run() {
        try {
            chatHandlers = new HashMap<>();
            Random random = new Random();
            serverSocket = new ServerSocket(0);
            DataManager.addServerSocket(serverSocket);
            setPortNumber(serverSocket.getLocalPort());
            System.out.println("Now the channel is listening and waiting for clients...");
            Socket socket;
            int handler_ID;
            while (true) {
                socket = serverSocket.accept();
                System.out.println("A user is entered!");
                handler_ID = random.nextInt();
                ChannelHandler channelHandler = new ChannelHandler(socket, handler_ID,this);
                chatHandlers.put(handler_ID, channelHandler);
                System.out.println("Before creating a new thread!");
                new Thread(channelHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getChannelName() {
        return channelName;
    }

    public Server getParentServer() {
        return parentServer;
    }
}
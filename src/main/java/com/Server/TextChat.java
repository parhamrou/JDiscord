package com.Server;

import com.Common.*;

import java.io.*;
import java.net.*;
import java.util.*;

public abstract class TextChat implements Serializable{

    protected transient Map<Integer, ChatHandler> chatHandlers; // must update
    protected ArrayList<Message> messages;
    protected ArrayList<String> filePaths;
    private transient Integer portNumber;
    protected transient ServerSocket serverSocket;
    private boolean isActive;
    private transient static Socket currentSocket;


    public TextChat() {
        messages = new ArrayList<>();
        filePaths = new ArrayList<>();
        isActive = true;
    }


    public static void fileServerSocketListen() {
        try {
            ServerSocket tempServerSocket = new ServerSocket(7000);
            DataManager.addServerSocket(tempServerSocket);
            Socket tempSocket;
            while (true) {
                tempSocket = tempServerSocket.accept();
                System.out.println("There is a request for working with files!");
                currentSocket = tempSocket;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static Socket getCurrentSocket() {
        return currentSocket;
    }

    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }

    public synchronized Integer getPortNumber() {
        return portNumber;
    }


    public synchronized void addMessage(Message message) {
        messages.add(message);
        System.out.println("The message is added!");
    }

    public synchronized void addFile(String filePath) {
        filePaths.add(filePath);
    }


    public ArrayList<Message> getMessages() {
        return messages;
    }


    public synchronized void sendMessage(Message message, int handler_ID) throws IOException {
        for (Map.Entry<Integer, ChatHandler> entry : chatHandlers.entrySet()) {
            if ((!entry.getKey().equals(handler_ID)) && entry.getValue().getIsOnline()) {
                entry.getValue().getObjectOutputStream().writeObject(message);
            }
        }
    }

    public synchronized void removeHandlerMap(int handler_ID) {
        chatHandlers.remove(handler_ID);
    }


    public ArrayList<String> getFilePaths() {
        return filePaths;
    }


    public  FileMessage doesFileExist(String filename) {
        for (Message message : messages) {
            if (message.getText().equalsIgnoreCase(filename)) {
                return (FileMessage) message;
            }
        }
        return null;
    }

    public void inActiveChat() {
        isActive = false;
    }

    public boolean getIsActive() {
        return isActive;
    }

}

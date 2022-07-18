package com.PVChat;

import java.io.*;

import com.Client.*;
import com.Common.*;

/**
 * This class is for handling one PV chat and showing its options when a user is working with that.
 */
public class PVHandler extends ChatHandler {

    private static PVHandler instance;
    private String otherUserUsername;

    public PVHandler(int portNumber, User user) {
        super(portNumber, user, false);
        instance = this;
    }

    public static PVHandler getInstance() {
        return instance;
    }

    public static void setInstanceNull() {
        instance = null;
    }

    public void start() throws IOException, ClassNotFoundException, InterruptedException {
        outputStream.writeObject(user.getUsername());
        otherUserUsername = (String) oInputStream.readObject();
        outputStream.writeObject(Request.LOAD_CHAT);
        boolean isSuccessful = (boolean) oInputStream.readObject();
        if (!isSuccessful) {
            System.out.println("Your friend has blocked you, so you can't send any messages to him/her!");
            return;
        }
        PVController.getInstance().setMessages(loadChat());
        outputStream.writeObject(Request.ENTER_CHAT);
        runThread();
        return;
    }

    public void exit() throws IOException, ClassNotFoundException {
        PVHandler.getInstance().serverWriter("#exit", null);
        outputStream.writeObject(Request.EXIT);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        outputStream.close();
        oInputStream.close();
        socket.close();
    }

    public String getOtherUserUsername() {
        return otherUserUsername;
    }
    /**
     * This method is for sending the 'block' request to the server.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void block() throws IOException, ClassNotFoundException {
        outputStream.writeObject(Request.BLOCK_IN_PV);
        outputStream.writeObject(user.getUsername());
        System.out.println("Now this user is blocked!");
    }
}

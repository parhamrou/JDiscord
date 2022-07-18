package com.Channel;

import com.Client.*;
import com.Common.*;
import com.PVChat.*;

import java.io.*;

public class ChannelHandler extends ChatHandler {

    private static ChannelHandler instance;
    private String channelName;

    public ChannelHandler(int portNumber, User user) {
        super(portNumber, user, true);
        instance = this;
    }

    public static ChannelHandler getInstance() {
        return instance;
    }

    public static void setInstanceNull() {
        instance = null;
    }

    public void start() throws IOException, ClassNotFoundException, InterruptedException {
        channelName = (String) oInputStream.readObject();
        System.out.println("Channel name received: " + channelName);
        outputStream.writeObject(Request.LOAD_CHAT);
        oInputStream.readObject();
        ChannelController.getInstance().setMessages(loadChat());
        outputStream.writeObject(Request.ENTER_CHAT);
        oInputStream.readObject();
        runThread();
        return;
    }

    public void exit() throws IOException, ClassNotFoundException {
        ChannelHandler.getInstance().serverWriter("#exit", null);
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

    public String getChannelName() {
        return channelName;
    }

}

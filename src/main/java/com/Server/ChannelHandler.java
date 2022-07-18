package com.Server;

import com.Common.*;

import java.io.*;
import java.net.*;

public class ChannelHandler extends ChatHandler implements Runnable, Serializable{

    private final TextChannel textChannel;


    public ChannelHandler(Socket socket, int handler_ID, TextChannel textChat) {
        super(socket, handler_ID, textChat);
        this.textChannel = textChat;
    }

    @Override
    public void run() {
        try {
            objectOutputStream.writeObject(textChannel.getChannelName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Request clientRequest;
        boolean response;
        while (true) {
            try {
                System.out.println("Waiting for request in Channelhandler in server side...");
                clientRequest = (Request) objectInputStream.readObject();
                System.out.println("Request: " + clientRequest.toString());
                if (!chat.getIsActive()) {
                    if (clientRequest == Request.BACK) {
                        return;
                    }
                    else {
                        response = false;
                        objectOutputStream.writeObject(response);
                        continue;
                    }
                }  else {
                    response = true;
                    objectOutputStream.writeObject(response);
                }
                switch (clientRequest) {
                    case LOAD_CHAT:
                        objectOutputStream.writeUnshared(chat.getMessages());
                        break;
                    case ENTER_CHAT:
                        isOnline = true;
                        Receiver();
                        isOnline = false;
                        break;
                    case EXIT:
                        objectOutputStream.close();
                        objectInputStream.close();
                        chat.removeHandlerMap(handler_ID);
                        socket.close();
                        return;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

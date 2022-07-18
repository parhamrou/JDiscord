package com.Server;
import com.Common.*;

import java.io.*;
import java.net.*;

public class PVHandler extends ChatHandler implements Runnable, Serializable{

    private final PVChat pvChat;


    public PVHandler(PVChat pvChat, Socket socket, int handler_ID) {
        super(socket, handler_ID, pvChat);
        this.pvChat = pvChat;
    }



    @Override
    public void run() {
        try {
            this.username = (String) objectInputStream.readObject();
            if (pvChat.getUser1().getUsername().equalsIgnoreCase(this.username)) {
                objectOutputStream.writeObject(pvChat.getUser2().getUsername());
            } else {
                objectOutputStream.writeObject(pvChat.getUser1().getUsername());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Request clientRequest;
        while (true) {
            try {
                System.out.println("Waiting for request in PVhandler in server side...");
                clientRequest = (Request) objectInputStream.readObject();
                System.out.println("Request: " + clientRequest.toString());
                switch (clientRequest) {
                    case BLOCK_IN_PV:
                        pvChat.block((String) objectInputStream.readObject());
                        break;
                    case LOAD_CHAT:
                        if (pvChat.isBlocked(username)) {
                            objectOutputStream.writeObject(false);
                            break;
                        } else {
                            objectOutputStream.writeObject(true);
                        }
                        objectOutputStream.writeUnshared(pvChat.getMessages());
                        break;
                    case ENTER_CHAT:
                        isOnline = true;
                        Receiver();
                        isOnline = false;
                        break;
                    case EXIT:
                        objectOutputStream.close();
                        objectInputStream.close();
                        pvChat.removeHandlerMap(handler_ID);
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

package com.Server;

import com.Common.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.concurrent.*;

public abstract class ChatHandler {

    protected String username;
    protected final int handler_ID;
    protected final Socket socket;
    protected ObjectOutputStream objectOutputStream;
    protected ObjectInputStream objectInputStream;
    protected boolean isOnline;
    protected TextChat chat;

    public ChatHandler(Socket socket, int handler_ID, TextChat chat) {
        this.socket = socket;
        this.handler_ID = handler_ID;
        this.chat = chat;
        this.isOnline = false;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }


    public void Receiver() throws IOException, ClassNotFoundException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        TextMessage message;
        Request request;
        while (true) {
            request = (Request) objectInputStream.readObject();
            System.out.println("Request: " + request.toString());
            if (request == Request.TEXT_MESSAGE) {
                message = (TextMessage) objectInputStream.readObject();
                if (message.getText().equalsIgnoreCase("#exit")) {
                    System.out.println("Exit received!");
                    objectOutputStream.writeObject(message);
                    return;
                }
                chat.addMessage(message);
                chat.sendMessage(message, handler_ID);
            } else if (request == Request.SEND_FILE){
                executorService.execute(this::saveFile);
            } else {
                executorService.execute(() -> {
                    try {
                        sendFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private void saveFile() {
        try {
            Thread.sleep(200);
            Socket currentSocket = chat.getCurrentSocket();
            ObjectInputStream objectInputStream = new ObjectInputStream(currentSocket.getInputStream());
            FileMessage fileMessage = (FileMessage) objectInputStream.readObject();
            File file = new File("D:\\data\\" + fileMessage.getText());
            Files.write(file.toPath(), fileMessage.getContent());
            chat.addFile(fileMessage.getText());
            chat.addMessage(fileMessage);
            chat.sendMessage(fileMessage, handler_ID);
            System.out.println("File is saved!");
            objectInputStream.close();
            currentSocket.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendFile() throws IOException, ClassNotFoundException {
        Socket currentSocket = chat.getCurrentSocket();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(currentSocket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(currentSocket.getInputStream());
        Request request = (Request) objectInputStream.readObject();
        if (request == Request.CHECK_FILE) {
            String fileName = (String) objectInputStream.readObject();
            FileMessage fileMessage = chat.doesFileExist(fileName);
            if (fileMessage == null) {
                objectOutputStream.writeObject(false);
            } else {
                objectOutputStream.writeObject(true);
                objectOutputStream.writeObject(fileMessage);
            }
        }
        objectInputStream.close();
        objectOutputStream.close();
        currentSocket.close();
    }

    public boolean getIsOnline() {
        return isOnline;
    }
}

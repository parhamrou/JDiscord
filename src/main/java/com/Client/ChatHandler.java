package com.Client;

import com.Channel.*;
import com.Common.*;
import com.PVChat.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public abstract class ChatHandler {

    protected User user;
    protected Socket socket;
    protected ObjectInputStream oInputStream;
    protected ObjectOutputStream outputStream;
    protected int portNumber;
    protected Scanner scanner = new Scanner(System.in);
    private boolean isChannel;

    // constructor
    public ChatHandler(int portNumber, User user, boolean isChannel) {
        try {
            this.user = user;
            this.isChannel = isChannel;
            this.portNumber = portNumber;
            socket = new Socket("localhost", portNumber); // now, this class connects with socket to the PVHandler class in server side
            oInputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void serverReader() throws ClassNotFoundException {
        try {
            Message message;
            while (true) {
                message = (Message) oInputStream.readObject();
                if (message.getText().equalsIgnoreCase("#exit")) {
                    System.out.println("Exit received! in server reader method");
                    return;
                }
                if (isChannel) {
                    ChannelController.getInstance().addMessage(message, false);
                } else {
                    PVController.getInstance().addMessage(message, false);
                }
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void serverWriter(String messageText, String fileDetail) throws ClassNotFoundException {
        try {
            if (messageText.equalsIgnoreCase("#send file")) {
                outputStream.writeObject(Request.SEND_FILE);
                Socket socket = new Socket("localhost", 7000); // connecting to the file serverSocket
                // this thread gets sends file in different thread
                new Thread(() -> sendFile(fileDetail, socket)).start();
            } else if (messageText.equalsIgnoreCase("#download file")) {
                outputStream.writeObject(Request.DOWNLOAD_FILE);
                Socket socket = new Socket("localhost", 7000);
                downloadFile(socket, fileDetail);
            } else {
                outputStream.writeObject(Request.TEXT_MESSAGE);
                outputStream.writeObject(new TextMessage(user, messageText));
            }
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void runThread() throws IOException, ClassNotFoundException {
        // this thread is for reading the messages from other users and printing them.
        Thread read = new Thread(() -> {
            try {
                serverReader();
            } catch (ClassNotFoundException e) {
                System.out.println(e);
            }
        });
        read.start();
    }

    protected ArrayList<Message> loadChat() throws IOException, ClassNotFoundException {
        return (ArrayList<Message>) oInputStream.readObject();
    }


    private void sendFile(String filePath, Socket socket) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            File file = new File(filePath);
            byte[] content = Files.readAllBytes(file.toPath());
            objectOutputStream.writeObject(new FileMessage(user, file.getName(), content));
            objectOutputStream.close();
            socket.close();
            System.out.println("The file is sent!");
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadFile(Socket socket, String fileName) throws IOException, ClassNotFoundException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        String path = "D:\\data";
        objectOutputStream.writeObject(Request.CHECK_FILE);
        objectOutputStream.writeObject(fileName);
        boolean doesFileExist = (boolean) objectInputStream.readObject();
        if (!doesFileExist) {
            System.out.println("There is no file with this name!");
            return;
        }
        new Thread(() -> {
            try {
                FileMessage fileMessage = (FileMessage) objectInputStream.readObject();
                File file = new File(path + fileMessage.getText());
                Files.write(file.toPath(), fileMessage.getContent());
                objectInputStream.close();
                objectOutputStream.close();
                socket.close();
                System.out.println("Download is finished");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
        System.out.println("Downloading...");
    }
}

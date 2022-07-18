package com.Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class MainServer {
    public static void main(String[] args) {
        try {
            DataManager.initialize();
            Scanner scanner = new Scanner(System.in);
            ExecutorService executorService = Executors.newCachedThreadPool();
            ServerSocket serverSocket = new ServerSocket(2000);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    TextChat.fileServerSocketListen();
                }
            });
            ClientHandler clientHandler;
            executorService.execute(() -> {
                while (true) {
                    Socket socket = null;
                    try {
                        socket = serverSocket.accept();
                    } catch (IOException e) {
                        System.out.println("The server is closed!");
                    }
                    System.out.println("A client is connected to the main ServerSocket with port number: " + socket.getLocalPort());
                    executorService.execute(new ClientHandler(socket));
                }
            });
            System.out.println("Press enter to stop the server!");
            scanner.nextLine();
            executorService.shutdownNow();
            DataManager.finalization();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Connection Error!");
            e.printStackTrace();
        }
    }
}

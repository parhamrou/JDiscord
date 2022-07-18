package com.Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

import com.Common.*;


/**
 * This class loads data from the files just after the server starts to work.
 * It keeps data in different lists.
 */
public class DataManager {

    private static final String usersFileAddress = "D:\\Discord\\src\\main\\java\\com\\Server\\data.DAT";

    private static List<User> users = Collections.synchronizedList(new ArrayList<>());
    private static List<Server> servers = Collections.synchronizedList(new ArrayList<>());
    private static List<ServerSocket> serverSockets = Collections.synchronizedList(new ArrayList<>());
    private static List<PVChat>  pvChats = Collections.synchronizedList(new ArrayList<>());

    /**
     * This \method calls the methods which have to load data from multiple files.
     */
    public static void initialize() {
        try {
            readUsers();
            runPVChats();
            runChannels();
            System.out.println("Ready to go...");
        }catch (ClassNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public static void finalization() {
        closeServerSockets();
        writeUsers();
        System.out.println("Finished...");
    }


    private static void readUsers() throws ClassNotFoundException {
        try {
            FileInputStream fInputStream = new FileInputStream(usersFileAddress);
            ObjectInputStream objectInputStream = new ObjectInputStream(fInputStream);
            while (true) {
                User tempUser = (User) objectInputStream.readObject();
                if (tempUser == null) {
                    break;
                }
                users.add(tempUser);
                ArrayList<PVChat> PVChats = tempUser.GetPvChats();
                for (PVChat pvChat : PVChats) {
                    if (!pvChats.contains(pvChat)) {
                        pvChats.add(pvChat);
                    }
                }
                ArrayList<Server> Servers = tempUser.getServers();
                for (Server server : Servers) {
                    if (!servers.contains(server)) {
                        servers.add(server);
                    }
                }
            }
            fInputStream.close();
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (EOFException e) {
            System.out.println("End of reading!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void writeUsers() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(usersFileAddress);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            for (User user : users) {
                objectOutputStream.writeObject(user);
            }
            fileOutputStream.close();
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static void runChannels() {
        for (Server server : servers) {
            server.runChannels(); // running the server's channels
        }
    }


    private static void runPVChats() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (PVChat pvChat : pvChats) {
            executorService.execute(pvChat);
            System.out.println("ran");
        }
    }


    public synchronized static boolean isServerNameDuplicated(String serverName) {
        for (Server server : servers) {
            if (server.getName().equalsIgnoreCase(serverName)) {
                return true;
            }
        }
        return false;
    }


    public synchronized static boolean isUserNameDuplicate(String userName) {
        for (User user : users) {
            if (userName.equalsIgnoreCase(user.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean checkUserName(HashMap<String, String> newAccount) {
        String userName = newAccount.get("username");
        return isUserNameDuplicate(userName);
    }

    public synchronized static boolean signUp(User newAccount) {
        return addUser(newAccount);
    }


    public static User signIn(HashMap<String, String> account) {
        String userName = account.get("username");
        String passWord = account.get("password");
        for (User user: users) {
            if (userName.equalsIgnoreCase(user.getUsername()) && passWord.equals(user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public synchronized static List<Server> getServers() {
        return servers;
    }


    public synchronized static List<User> getUsers() {
        return users;
    }

    private synchronized static  boolean addUser(User user) {
        users.add(user);
        return true;
    }

    public synchronized static void addServerSocket(ServerSocket serverSocket) {
        serverSockets.add(serverSocket);
    }

    private static void closeServerSockets() {
        for (ServerSocket serverSocket : serverSockets) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized static User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }


    public synchronized static void addServer(Server server) {
        System.out.println("The server is added!");
        servers.add(server);
    }

    public synchronized static void addPVChat(PVChat pvChat) {
        pvChats.add(pvChat);
    }
    public static void removeServer(Server server) {
        servers.remove(server);
    }
}

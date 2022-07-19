package com.Menu;

import com.Channel.*;
import com.Client.*;
import com.Common.*;
import com.PVChat.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class AppController implements Runnable {

    private static AppController instance;
    private User user;
    private String hostname;
    private int portNumber;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream objectInputStream;


    // constructor
    private AppController(String hostname, int portNumber) throws IOException {
        this.hostname = hostname;
        this.portNumber = portNumber;
        socket = new Socket(hostname, portNumber);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {

    }

    public static AppController getInstance() {
        if (instance == null) {
            try {
                instance = new AppController("localhost", 2000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }


    public void setUser(User user) {
        this.user = user;
    }


    public User getUser() {
        return user;
    }


    public Socket getSocket() {
        return socket;
    }

    public void closeSocket() throws IOException {
        socket.close();
    }

    public boolean login(Map<String, String> userMap) throws IOException, ClassNotFoundException {
        int result = sendFirstMap(userMap, true);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean signUp(Map<String, String> userMap, String email) throws IOException, ClassNotFoundException {
        int result = sendFirstMap(userMap, false);
        if (result == 0) {
            boolean isSignUpSuccessful = sendSecondMap(email);
            if (isSignUpSuccessful) {
                System.out.println("You signed up successfully!");
                return true;
            } else {
                System.out.println("Our server had problems signing you up!");
                return false;
            }
        } else {
            System.out.println("You have logged in before. You can login now!");
            return false;
        }
    }

    private boolean sendSecondMap(String email) throws IOException, ClassNotFoundException {
        outputStream.writeObject(Request.SIGN_UP);
        user.setEmail(email);
        outputStream.writeObject(user);
        return (boolean) objectInputStream.readObject();
    }


    private int sendFirstMap(Map<String, String> userMap, boolean isLogin) throws IOException, ClassNotFoundException {
        outputStream.writeObject(Request.CHECK_USERNAME);
        outputStream.writeObject(userMap); // sending the Map to the server to check the validity
        boolean isUsernameDuplicated = (Boolean) objectInputStream.readObject();
        if (isUsernameDuplicated) { // reading the request's answer from the server
            if (isLogin) {
                outputStream.writeObject(Request.SIGN_IN);
                outputStream.writeObject(userMap);
                this.user = (User) objectInputStream.readObject(); // setting the user field
                return 0;
            } else {
                return -2;
            }
        } else {
            if (isLogin) {
                return -1;
            } else {
                this.user = new User(userMap.get("username"), userMap.get("password"), null, null);
                return 0;
            }
        }
    }

    public boolean ChangeUsername(String username) {
        try {
            outputStream.writeObject(Request.CHANGE_USERNAME);
            outputStream.writeObject(username);
            boolean isDuplicated = (boolean) objectInputStream.readObject();
            if (!isDuplicated) {
                user.setUsername(username);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void changeEmail(String email) {
        try {
            outputStream.writeObject(Request.CHANGE_EMAIL);
            outputStream.writeObject(email);
            user.setEmail(email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changePhoneNumber(String phoneNumber) {
        try {
            outputStream.writeObject(Request.CHANGE_PHONE_NUMBER);
            outputStream.writeObject(phoneNumber);
            user.setPhoneNumber(phoneNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void changePassword(String password) {
        try {
            outputStream.writeObject(Request.CHANGE_PASSWORD);
            outputStream.writeObject(password);
            user.setPassword(password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getServersList() {
        try {
            outputStream.writeObject(Request.SERVER_LIST);
            return (ArrayList<String>) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getPVChatsList() {
        try {
            outputStream.writeObject(Request.PV_LIST);
            return (ArrayList<String>) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String, byte[]> getPVChatsList(boolean empty) {
        try {
            outputStream.writeObject(Request.PV_MAP_LIST);
            return (HashMap<String, byte[]>) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void enterPV(String PVName) {
        try {
            outputStream.writeObject(Request.ENTER_CHAT); // sending request
            outputStream.writeObject(PVName); // sending server name
            boolean isSuccessful = (boolean) objectInputStream.readObject();
            if (isSuccessful) {
                Integer portNumber = (Integer) objectInputStream.readObject();
                cleanChatControllers();
                new PVHandler(portNumber, user).start(); // now we are connected to the PV socket!
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public boolean addNewFriend(String username) {
        try {
            outputStream.writeObject(Request.ADD_NEW_FRIEND);
            outputStream.writeObject(username);
            return (boolean) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<FriendshipRequest> showFriendshipRequests() {
        try {
            outputStream.writeObject(Request.SHOW_FRIENDSHIP_REQESTS);
            return (List<FriendshipRequest>) objectInputStream.readUnshared();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendRequestAnswer(String username, boolean isAccepted) {
        try {
            outputStream.writeObject(Request.ANSWER_FRIENDSHIP_REQUEST);
            outputStream.writeObject(username);
            if (isAccepted) {
                outputStream.writeObject(Request.ACCEPT_REQUEST);
            } else {
                outputStream.writeObject(Request.REJECT_REQUEST);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getFriendsList() {
        try {
            outputStream.writeObject(Request.FRIENDS_LIST);
            return (ArrayList<String>) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String, byte[]> getAddPVChatList() {
        try {
            outputStream.writeObject(Request.FRIENDS_WITH_NO_CHAT_LIST);
            return (HashMap<String, byte[]>) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createPVChat(String username) {
        try {
            outputStream.writeObject(Request.CREATE_PV_CHAT);
            outputStream.writeObject(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cleanChatControllers() {
        try {
            if (PVHandler.getInstance() != null) {
                PVHandler.getInstance().exit();
                PVHandler.setInstanceNull();
                PVController.setInstanceNull();
            }
            if (com.Channel.ChannelHandler.getInstance() != null) {
                com.Channel.ChannelHandler.getInstance().exit();
                com.Channel.ChannelHandler.setInstanceNull();
                ChannelController.setInstanceNull();
            }
            if (ServerHandler.getInstance() != null) {
                ServerHandler.getInstance().exit();
                ServerHandler.getInstance().setInstanceNull();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean createServer(String serverName) {
        try {
            outputStream.writeObject(Request.CHECK_SERVER_NAME);
            outputStream.writeObject(serverName);
            boolean isDuplicated = (Boolean) objectInputStream.readObject();
            if (!isDuplicated) {
                outputStream.writeObject(Request.CREATE_SERVER);
                outputStream.writeObject(user);
                System.out.println("Now the server is created!");
                return true;
            } else {
                outputStream.writeObject(Request.BACK);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void enterServer(String serverName) {
        try {
            outputStream.writeObject(Request.ENTER_SERVER); // sending request
            outputStream.writeObject(serverName); // sending server name
            boolean isSuccessful = (boolean) objectInputStream.readObject();
            if (isSuccessful) {
                ServerHandler serverHandler = new ServerHandler(user, serverName, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void changeAvatar(byte[] content) {
        try {
            outputStream.writeObject(Request.CHANGE_AVATAR);
            outputStream.writeObject(content);
            user.setAvatar(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        try {
            outputStream.writeObject(Request.EXIT);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
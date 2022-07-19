package com.Server;

import java.io.*;
import java.net.*;
import java.util.*;

import com.Common.*;

public class ClientHandler implements Runnable {


    private ObjectInputStream oInputStream;
    private ObjectOutputStream oOutputStream;
    private final Socket socket;
    private User user;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            oOutputStream = new ObjectOutputStream(socket.getOutputStream());
            oInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("I/O Error!");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Request clientRequest;
            HashMap<String, String> userMap;
            while (true) {
                System.out.println("Waiting for a request from the user " + "...");
                clientRequest = (Request) oInputStream.readObject();
                System.out.println("Thread ID:" + Thread.currentThread().getId());
                System.out.println("Request: " + clientRequest.toString());
                System.out.println("______________________");
                switch (clientRequest) {
                    case CHECK_USERNAME:
                        userMap = (HashMap<String, String>) oInputStream.readObject();
                        oOutputStream.writeObject(DataManager.checkUserName(userMap));
                        break;
                    case CHANGE_USERNAME:
                        changeUsername();
                        break;
                    case CHANGE_EMAIL:
                        user.setEmail((String) oInputStream.readObject());
                        break;
                    case CHANGE_PASSWORD:
                        user.setPassword((String) oInputStream.readObject());
                        break;
                    case CHANGE_PHONE_NUMBER:
                        user.setPhoneNumber((String) oInputStream.readObject());
                        break;
                    case CHANGE_AVATAR:
                        user.setAvatar((byte[]) oInputStream.readObject());
                        break;
                    case SIGN_UP:
                        user = (User) oInputStream.readObject();
                        oOutputStream.writeObject(DataManager.signUp(user));
                        break;
                    case SIGN_IN:
                        userMap = (HashMap<String, String>) oInputStream.readObject();
                        user = DataManager.signIn(userMap);
                        oOutputStream.writeObject(user);
                        break;
                    case SIGN_OUT:
                        user = null;
                        break;
                    case SERVER_LIST:
                        getServerList();
                        break;
                    case ENTER_SERVER:
                        enterServer();
                        break;
                    case FRIENDS_LIST:
                        getFriendsList();
                        break;
                    case CHECK_SERVER_NAME:
                        createServer();
                        break;
                    case ADD_NEW_FRIEND:
                        addNewFriend();
                        break;
                    case SHOW_FRIENDSHIP_REQESTS:
                        showFriendshipRequests();
                        break;
                    case ANSWER_FRIENDSHIP_REQUEST:
                        answerFriendshipRequest();
                        break;
                    case FRIENDS_WITH_NO_CHAT_LIST:
                        getAddPvList();
                        break;
                    case PV_LIST:
                        getPVList();
                        break;
                    case PV_MAP_LIST:
                        getPVMapList();
                        break;
                    case ENTER_CHAT:
                        enterPV();
                        break;
                    case CREATE_PV_CHAT:
                        createPVChat();
                        break;
                    case EXIT:
                        socket.close();
                        return;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error");
        }
    }


    private void getServerList() throws IOException, ClassNotFoundException {
        oOutputStream.writeUnshared(user.getServersNames());
    }

    private void createServer() throws IOException, ClassNotFoundException {
        String serverName = (String) oInputStream.readObject();
        oOutputStream.writeObject(DataManager.isServerNameDuplicated(serverName));
        Request request = (Request) oInputStream.readObject();
        if (request == Request.CREATE_SERVER) {
            User user = (User) oInputStream.readObject();
            Server server = new Server(serverName, user);
            DataManager.addServer(server); // adding the new server to the Database
            user.addServer(server); // adding the new server to the user's servers list
        }
    }

    private void enterServer() throws IOException, ClassNotFoundException {
        String serverName = (String) oInputStream.readObject(); // getting the server's name from client
        boolean doesServerExist = user.doesServerExist(serverName);
        oOutputStream.writeObject(doesServerExist);
        if (doesServerExist) {
            new ServerHandler(user.getServer(serverName), socket, this.user).start();
        }
    }

    private void addNewFriend() throws IOException, ClassNotFoundException {
        String username = (String) oInputStream.readObject();
        boolean doesUserExist = (DataManager.isUserNameDuplicate(username));
        boolean isSuccessful = true;
        if (doesUserExist) {
            if (DataManager.getUser(username) == this.user)
                isSuccessful = false;
        }
        oOutputStream.writeObject(isSuccessful);
        if (isSuccessful) {
            DataManager.getUser(username).addFriendshipRequest(new FriendshipRequest(DataManager.getUser(username), this.user));
        }
    }

    private void answerFriendshipRequest() throws IOException, ClassNotFoundException {
        String chosenUsername = (String) oInputStream.readObject();
        FriendshipRequest friendshipRequest = user.getFriendshipRequest(chosenUsername);
        Request request2 = (Request) oInputStream.readObject();
        if (request2 == Request.ACCEPT_REQUEST) {
            user.addFriend(friendshipRequest.getSenderUser());
            friendshipRequest.getSenderUser().addFriend(this.user);
        }
        user.removeFriendshipRequest(friendshipRequest);
    }

    private void showFriendshipRequests() throws IOException {
        oOutputStream.reset();
        oOutputStream.writeUnshared(user.getFriendshipRequests());
        System.out.println(user.getFriendshipRequests());
    }

    private void getPVList() throws IOException, ClassNotFoundException {
        oOutputStream.writeUnshared(user.getPvChats());
    }

    private void getPVMapList() throws IOException {
        HashMap<String, byte[]> userMaps = new HashMap<>();
        for (PVChat pvChat : user.getPvChats(true)) {
            String username;
            byte[] image;
            if (pvChat.getUser1().getUsername().equalsIgnoreCase(user.getUsername())) {
                username = pvChat.getUser2().getUsername();
                image = pvChat.getUser2().getAvatar();
            } else {
                username = pvChat.getUser1().getUsername();
                image = pvChat.getUser1().getAvatar();
            }
            userMaps.put(username, image);
        }
        oOutputStream.writeUnshared(userMaps);
    }

    private void getFriendsList() throws IOException {
        oOutputStream.writeObject(user.getFriendsList());
    }


    private void changeUsername() throws IOException, ClassNotFoundException {
        String newUsername = (String) oInputStream.readObject();
        boolean isDuplicated = DataManager.isUserNameDuplicate(newUsername);
        oOutputStream.writeObject(isDuplicated);
        if (!isDuplicated) {
            user.setUsername(newUsername);
        }
    }

    private void getAddPvList() throws IOException {
        oOutputStream.writeUnshared(user.getAddPVChatList());
    }

    private void createPVChat() throws IOException, ClassNotFoundException {
        String chatName = (String) oInputStream.readObject();
        PVChat pvchat = new PVChat(this.user, user.getFriend(chatName));
        user.addPvChat(pvchat);
        user.getFriend(chatName).addPvChat(pvchat);
        DataManager.addPVChat(pvchat);
        new Thread(pvchat).start();
    }

    private void enterPV() throws IOException, ClassNotFoundException {
        String PVName = (String) oInputStream.readObject();
        PVChat pvChat = user.getPVChat(PVName);
        boolean doesPVChatExist;
        if (pvChat == null) {
            doesPVChatExist = false;
        } else {
            doesPVChatExist = true;
        }
        oOutputStream.writeObject(doesPVChatExist);
        if (doesPVChatExist) {
            oOutputStream.writeObject(pvChat.getPortNumber());
        }
    }
}



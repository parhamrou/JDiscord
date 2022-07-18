package com.Server;

import com.Common.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class ServerHandler {

    private Server server;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private User user;

    // constructor
    public ServerHandler(Server server, Socket socket, User user) {
        this.server = server;
        this.socket = socket;
        this.user = user;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void start() {
        Request clientRequest;
        boolean response;
        while (true) {
            try {
                System.out.println("Waiting for request in serverHandler in sevrer side!");
                clientRequest = (Request) objectInputStream.readObject();
                System.out.println("Request: " + clientRequest.toString());
                if (!server.getIsActive()) {
                    if (clientRequest == Request.BACK) {
                        response = true;
                        objectOutputStream.writeObject(response);
                        return;
                    } else {
                        response = false;
                        objectOutputStream.writeObject(response);
                        continue;
                    }
                } else {
                    response = true;
                    objectOutputStream.writeObject(response);
                }
                switch (clientRequest) {
                    case CHANNEL_LIST:
                        objectOutputStream.writeObject(server.getChannelsNames());
                        break;
                    case CHECK_CHANNEL_NAME:
                        String channelName = (String) objectInputStream.readObject();
                        boolean isDuplicated = server.isChannelNameDuplicated(channelName);
                        objectOutputStream.reset();
                        objectOutputStream.writeUnshared(isDuplicated);
                        Request request = (Request) objectInputStream.readUnshared();
                        if (request == Request.CREATE_CHANNEL) {
                            TextChannel channel = new TextChannel(channelName, server);
                            server.addChannel(channel); // adding the new server to the Database
                            new Thread(channel).start(); // running the channel
                        }
                        break;
                    case GET_ROLE_NAMES:
                        objectOutputStream.writeUnshared(server.roleNames());
                        break;
                    case MAP_ROLE:
                        Map<String, String> rolePair = (Map<String, String>) objectInputStream.readObject();
                        User user1 = server.getUser(rolePair.get("username"));
                        Role role = server.getRole(rolePair.get("roleName"));
                        server.mapRole(user1, role);
                        break;
                    case ENTER_CHAT:
                        channelName = (String) objectInputStream.readObject();
                        boolean doesChannelExist = server.doesChannelExist(channelName);
                        objectOutputStream.writeObject(doesChannelExist);
                        if (doesChannelExist) {
                            objectOutputStream.writeObject(server.getChannelPortNumber(channelName));

                        }
                        break;
                    case GET_USERS_LIST:
                        ArrayList<String> usersList = server.getUsersNames();
                        usersList.remove(server.getCreator().getUsername());
                        objectOutputStream.writeObject(usersList);
                        break;
                    case CAN_CREATE_CHANNEL:
                        boolean canCreateChannel;
                        if (!server.getRoleMap().containsKey(user)) {
                            canCreateChannel = false;
                        } else {
                            canCreateChannel = server.getRoleMap().get(user).CanCreateChannel();
                        }
                        objectOutputStream.writeObject(canCreateChannel);
                        break;
                    case CAN_MAP_ROLE:
                        boolean isCreator = (server.getCreator() == this.user);
                        objectOutputStream.writeObject(isCreator);
                        break;
                    case CAN_REMOVE_CHANNEL:
                        boolean canRemoveChannel;
                        if (!server.getRoleMap().containsKey(user)) {
                            canRemoveChannel = false;
                        } else {
                            canRemoveChannel = server.getRoleMap().get(user).CanRemoveChannel();
                        }
                        objectOutputStream.writeObject(canRemoveChannel);
                        break;
                    case ADD_ROLE:
                        isCreator = (server.getCreator() == this.user);
                        objectOutputStream.writeObject(isCreator);
                        if ((Request) objectInputStream.readObject() == Request.CREATE_ROLE) {
                            server.addRole((Role) objectInputStream.readObject());
                        }
                        break;
                    case GET_NOT_ADDED_USERS:
                        ArrayList<String> notAddedUsers = new ArrayList<>();
                        for (String userName : user.getFriendsList()) {
                            if (!server.getUsersNames().contains(userName)) {
                                notAddedUsers.add(userName);
                            }
                        }
                        objectOutputStream.writeObject(notAddedUsers);
                        break;
                    case CAN_CREATE_ROLE:
                        isCreator = (server.getCreator() == this.user);
                        objectOutputStream.writeObject(isCreator);
                        break;
                    case CAN_REMOVE_USER:
                        boolean canRemove;
                        if (!server.getRoleMap().containsKey(user)) {
                            canRemove = false;
                        } else {
                            canRemove = server.getRoleMap().get(user).CanRemoveUser();
                        }
                        objectOutputStream.writeObject(canRemove);
                        break;
                    case CAN_REMOVE_SERVER:
                        isCreator = (server.getCreator() == this.user);
                        objectOutputStream.writeObject(isCreator);
                        break;
                    case CAN_CHANGE_NAME:
                        boolean canChange;
                        if (!server.getRoleMap().containsKey(user)) {
                            canChange = false;
                        } else {
                            canChange = server.getRoleMap().get(user).CanChangeName();
                        }
                        objectOutputStream.writeObject(canChange);
                        break;
                    case CREATE_ROLE:
                        server.addRole((Role) objectInputStream.readObject());
                        break;
                    case ADD_USER:
                        String username = (String) objectInputStream.readObject();
                        boolean doesFriendExist = user.doesFriendExist(username);
                        objectOutputStream.writeObject(doesFriendExist);
                        if (doesFriendExist) {
                            User friend = user.getFriend(username);
                            server.addUser(friend); // adding user to the server
                            friend.addServer(this.server); // adding server to the user's list
                        }
                        break;
                    case REMOVE_USER:
                        username = (String) objectInputStream.readObject();
                        server.removeUser(username);
                        break;
                    case CHANGE_SERVER_NAME:
                        server.setName((String) objectInputStream.readObject());
                        break;
                    case DELETE_CHANNEL:
                        channelName = (String) objectInputStream.readObject();
                        server.getChannel(channelName).inActiveChat();
                        server.removeChannel(server.getChannel(channelName));
                        break;
                    case DELETE_SERVER:
                        server.removeServerFromUsers(); // removing from users' lists
                        server.inActiveChannels();
                        server.inActiveServer();
                        DataManager.removeServer(server);
                        return;
                    case BACK:
                        System.out.println("Back from server handler in server side");
                        return;
                }
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }
}

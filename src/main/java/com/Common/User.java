package com.Common;


import java.io.Serializable;
import java.lang.reflect.*;
import java.util.*;

public class User implements Serializable{

    private String username;
    private String password;
    private String email;
    private String phoneNumber; // optional
    private String status;
    private ArrayList<Server> servers;
    private ArrayList<User> friends;
    private ArrayList<PVChat> pvChats;
    private List<FriendshipRequest> friendshipRequests;

    
    // constructor
    public User(String username, String password, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        friends = new ArrayList<>();
        servers = new ArrayList<>();
        pvChats = new ArrayList<>();
        friendshipRequests = Collections.synchronizedList(new ArrayList<>());
    }


    /**
     * This method is for showing the list of the friends of the user.
     */
    public void showFriends() {
        for (User user : friends) {
            System.out.println("- " + user.getUsername());
        }
    }


    public void addServer(Server server) {
        servers.add(server);
    } 


    public void setEmail(String email) {
        this.email = email;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }


    public ArrayList<Server> getServers() {
        return servers;
    }


    public ArrayList<String> getServersNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Server server : servers) {
            names.add(server.getName());
        }
        return names;
    }

    public boolean doesServerExist(String serverName) {
        for (Server server : servers) {
            if (server.getName().equalsIgnoreCase(serverName)) {
                return true;
            }
        }
        return false;
    }

    public boolean doesFriendExist(String username) {
        for (User friend : friends) {
            if (friend.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public User getFriend(String username) {
        for (User user : friends) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }


    public Server getServer(String serverName) {
        for (Server server : servers) {
            if (server.getName().equalsIgnoreCase(serverName)) {
                return server;
            }
        }
        return null;
    }


    public ArrayList<User> getFriends() {
        return friends;
    }

    public ArrayList<String> getFriendsList() {
        ArrayList<String> friends = new ArrayList<>();
        for (User user : this.friends) {
            friends.add(user.getUsername());
        }
        return friends;
    }


    public void addFriendshipRequest(FriendshipRequest friendshipRequest) {
        friendshipRequests.add(friendshipRequest);
    }


    public void addFriend(User friend) {
        friends.add(friend);
    }


    public boolean doesFriendshipRequestExist(String username) {
        for (FriendshipRequest friendshipRequest : friendshipRequests) {
            if (friendshipRequest.getSenderUser().getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }


    public FriendshipRequest getFriendshipRequest(String username) {
        for (FriendshipRequest friendshipRequest : friendshipRequests) {
            if (friendshipRequest.getSenderUser().getUsername().equalsIgnoreCase(username)) {
                return friendshipRequest;
            }
        }
        return null;
    }


    public void removeFriendshipRequest(FriendshipRequest friendshipRequest) {
        friendshipRequests.remove(friendshipRequest);
    }


    public ArrayList<String> getPvChats() {
        ArrayList<String> names = new ArrayList<>();
        for (PVChat pvChat : pvChats) {
            if (pvChat.getUser1().getUsername().equalsIgnoreCase(username)) {
                names.add(pvChat.getUser2().getUsername());
            } else {
                names.add(pvChat.getUser1().getUsername());
            }
        }
        return names;
    }

    public ArrayList<PVChat> GetPvChats() {
        return pvChats;
    }

    public List<FriendshipRequest> getFriendshipRequests() {
        return friendshipRequests;
    }


    public PVChat getPVChat(String username) {
        for (PVChat pvChat : pvChats) {
            String firstUser = pvChat.getUser1().getUsername();
            String secondUser = pvChat.getUser2().getUsername();
            if ((firstUser.equalsIgnoreCase(username) || secondUser.equalsIgnoreCase(username)) && !this.username.equalsIgnoreCase(username)) {
                return pvChat;
            }
        }
        return null;
    }

    public void addPvChat(PVChat pvChat) {
        pvChats.add(pvChat);
    }

    public void removeServer(Server server) {
        servers.remove(server);
    }

    public ArrayList<String> getAddPVChatList() {
        ArrayList<String> list = new ArrayList<>();
        for (User friend : friends) {
            if (getPVChat(friend.getUsername()) == null) {
                list.add(friend.getUsername());
            }
        }
        return list;
    }

}
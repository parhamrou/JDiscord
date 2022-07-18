package  com.Client;

import java.io.*;
import java.net.Socket;
import java.util.*;

import com.Channel.ChannelHandler;
import com.Common.*;
import com.Menu.*;

/**
 * This class has the responsibility to make the connection between one server and the client.
 * It gets the client's requests and passes them to the server's 'ServerHandler' class.
 */
public class ServerHandler {

    private static ServerHandler instance;

    private String serverName;
    private User user;
    private Socket socket;
    private Scanner scanner = new Scanner(System.in);
    private ObjectInputStream oInputStream;
    private ObjectOutputStream outputStream;
    // constructor
    public ServerHandler(User user, String serverName, Socket socket) {
        try {
            instance = this;
            this.user = user;
            this.serverName = serverName;
            this.socket = socket;
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            oInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static ServerHandler getInstance() {
        return instance;
    }

    public void setInstanceNull() {
        instance = null;
    }


    public ArrayList<String> getChannels() {
        try {
            outputStream.writeObject(Request.CHANNEL_LIST);
            if (!(boolean) oInputStream.readObject()) { // checking if the server exists or not
                System.out.println("This server doesn't exist anymore. Please Exit!");
                return null;
            }
            return  (ArrayList<String>) oInputStream.readObject();
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return null;
    }

    public void deleteChannel(String channelName) {
        try {
            outputStream.writeObject(Request.DELETE_CHANNEL);
            oInputStream.readObject();
            outputStream.writeObject(channelName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        try {
            outputStream.writeObject(Request.BACK);
            System.out.println((boolean) oInputStream.readObject());
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean createChannel(String channelName) throws ClassNotFoundException, IOException {
        outputStream.writeObject(Request.CHECK_CHANNEL_NAME);
        oInputStream.readObject();
        outputStream.writeObject(channelName);
        boolean isDuplicated = (boolean) oInputStream.readObject();
        System.out.println(isDuplicated);
        if (isDuplicated) { // checking if the server exists or not
            System.out.println("is duplicated!");
            outputStream.writeUnshared(Request.BACK);
            return false;
        }
        outputStream.writeUnshared(Request.CREATE_CHANNEL);
        return true;
    }

    public boolean canCreateChannel() {
        try {
            outputStream.writeObject(Request.CAN_CREATE_CHANNEL);
            oInputStream.readObject();
            return (boolean) oInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean canMapRole() {
        try {
            outputStream.writeObject(Request.CAN_MAP_ROLE);
            oInputStream.readObject();
            return (boolean) oInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void mapRole(String roleName, String username) {
        try {
            outputStream.writeObject(Request.MAP_ROLE);
            oInputStream.readObject();
            Map<String, String> rolepair = new HashMap<>();
            rolepair.put("roleName", roleName);
            rolepair.put("username", username);
            outputStream.writeObject(rolepair);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> getRoleNames() {
        try {
            outputStream.writeObject(Request.GET_ROLE_NAMES);
            oInputStream.readObject();
            return (ArrayList<String>) oInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean canCreateRole() {
        try {
            outputStream.writeObject(Request.CAN_CREATE_ROLE);
            oInputStream.readObject();
            return (boolean) oInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createRole(String roleName, boolean[] abilities) {
        try {
            outputStream.writeObject(Request.CREATE_ROLE);
            oInputStream.readObject();
            outputStream.writeObject(new Role(roleName, abilities));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void enterChannel(String channelName) {
        try {
            outputStream.writeObject(Request.ENTER_CHAT); // sending request
            oInputStream.readObject();
            outputStream.writeObject(channelName); // sending server name
            boolean isSuccessful = (boolean) oInputStream.readObject();
            if (isSuccessful) {
                Integer portNumber = (Integer) oInputStream.readObject();
                new ChannelHandler(portNumber, user).start(); // now we are connected to the PV socket!
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean canRemoveChannel() {
        try {
            outputStream.writeObject(Request.CAN_REMOVE_CHANNEL);
            oInputStream.readObject();
            return (boolean) oInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean canRemoveUser() {
        try {
            outputStream.writeObject(Request.CAN_REMOVE_USER);
            oInputStream.readObject();
            return (boolean) oInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeUser(String username) {
        try {
            outputStream.writeObject(Request.REMOVE_USER);
            oInputStream.readObject();
            outputStream.writeObject(username);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<String> getUsers() {
        try {
            outputStream.writeObject(Request.GET_USERS_LIST);
            oInputStream.readObject();
            return (ArrayList<String>) oInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean canChangeName() {
        try {
            outputStream.writeObject(Request.CAN_CHANGE_NAME);
            oInputStream.readObject();
            return (boolean) oInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void changeServerName(String newName) {
        try {
            outputStream.writeObject(Request.CHANGE_SERVER_NAME);
            oInputStream.readObject();
            outputStream.writeObject(newName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<String> getNotAddedFriends() throws IOException, ClassNotFoundException {
        outputStream.writeObject(Request.GET_NOT_ADDED_USERS);
        if (!(boolean) oInputStream.readObject()) { // checking if the server exists or not
            System.out.println("This server doesn't exist anymore. Please Exit!");
            return null;
        }
        return (ArrayList<String>) oInputStream.readObject();
    }

    public boolean addUser(String username) throws IOException, ClassNotFoundException {
        outputStream.writeObject(Request.ADD_USER);
        if (!(boolean) oInputStream.readObject()) { // checking if the server exists or not
            System.out.println("This server doesn't exist anymore. Please Exit!");
            return false;
        }
        outputStream.writeObject(username);
        boolean isUsernameValid = (boolean) oInputStream.readObject();
        if (!isUsernameValid) {
            System.out.println("There is no user with this username!");
            return false;
        }
        return true;
    }

    public boolean canRemoveServer() {
        try {
            outputStream.writeObject(Request.CAN_REMOVE_SERVER);
            oInputStream.readObject();
            return (boolean) oInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeServer() {
        try {
            outputStream.writeObject(Request.DELETE_SERVER);
            oInputStream.readObject();
            setInstanceNull();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}

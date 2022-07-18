package com.Common;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.*;

public class FriendshipRequest implements Serializable {

    private LocalDateTime date;
    private User senderUser;
    private User destinationUser;


    // constructor
    public FriendshipRequest(User destinationUser, User senderUser) {
        this.destinationUser = destinationUser;
        this.senderUser = senderUser;
        date = LocalDateTime.now();
    }

    public User getDestinationUser() {
        return destinationUser;
    }


    public User getSenderUser() {
        return senderUser;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = date.format(formatter);
        return String.format("%s\t%s", senderUser.getUsername(), formatDateTime);
    }

}

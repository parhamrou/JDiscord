package com.Common;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.*;

public abstract class Message implements Serializable {
    
    private User user;
    private LocalDateTime date;
    private int likes = 0;
    private int dislikes = 0;
    private int smiles = 0;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // constructor
    public Message(User user) {
        this.user = user;
        date = LocalDateTime.now();
    }

    public abstract String getText();

    public User getUser() {
        return user;
    }

    public String getDate() {
        return date.format(formatter);
    }

    @Override
    public String toString() {
        return user.toString();
    }
}

package com.Common;

import java.io.*;

public class Role implements Serializable {

    private String name;
    // capabilities
    private boolean canCreateChannel;
    private boolean canRemoveChannel;
    private boolean canRemoveUser;
    private boolean canLimitUser;
    private boolean canBanFromServer;
    private boolean canChangeName;
    private boolean canSeeHistory;
    private boolean canPin;

    
    // constructor
    public Role(String name ,boolean[] answers) {
        this.name = name;
        this.canCreateChannel = answers[0];
        this.canRemoveChannel = answers[1];
        this.canRemoveUser = answers[2];
        this.canLimitUser = answers[3];
        this.canBanFromServer = answers[4];
        this.canChangeName = answers[5];
        this.canSeeHistory = answers[6];
        this.canPin = answers[7];
    }

    public boolean CanCreateChannel() {
        return canCreateChannel;
    }

    public boolean CanRemoveChannel() {
        return canRemoveChannel;
    }

    public boolean CanRemoveUser() {
        return canRemoveUser;
    }

    public boolean CanLimitUser() {
        return canLimitUser;
    }

    public boolean CanBanFromServer() {
        return canBanFromServer;
    }

    public boolean CanChangeName() {
        return canChangeName;
    }

    public boolean CanSeeHistory() {
        return CanSeeHistory();
    }

    public boolean CanPin() {
        return CanPin();
    }

    public String getName() {
        return name;
    }
}

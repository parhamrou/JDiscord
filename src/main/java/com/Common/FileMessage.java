package com.Common;

import java.io.*;
import java.time.format.*;
import java.util.*;

public class FileMessage extends Message implements Serializable {

    private byte[] content;
    private String fileName;

    public FileMessage(User user, String fileName, byte[] content) {
        super(user);
        this.fileName = fileName;
        this.content = content;
    }

    @Override
    public String getText() {
        return fileName;
    }

    public byte[] getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("______________" +
                        "\n%s\t%s\n%s\n%s", getUser().getUsername(), getDate(), fileName,
                "______________");
    }
}

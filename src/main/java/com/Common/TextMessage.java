package com.Common;

import java.io.*;
import java.time.format.*;

public class TextMessage extends Message implements Serializable {

    private final String text;

    public TextMessage(User user, String text) {
        super(user);
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return String.format("______________" +
                        "\n%s\t%s\n%s\n%s", getUser().getUsername(), getDate(), text,
                "______________");
    }
}

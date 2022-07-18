package com.Menu;

import com.Client.*;
import com.MainPage.*;
import com.app.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.*;
import java.util.*;

public class LoginController {


    @FXML
    private Text infoAlertText;

    @FXML
    private Text passwordAlertText;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Text usernameAlertText;

    @FXML
    private TextField usernameTextField;

    @FXML
    void RegisterPressed(ActionEvent event) throws IOException {
        App.switchScene(event, LoginController.class.getResource("signUp.fxml"));
    }

    @FXML
    void loginButtonPressed(ActionEvent event) throws IOException, ClassNotFoundException {
        usernameAlertText.setText("");
        passwordAlertText.setText("");
        infoAlertText.setText("");
        if (usernameTextField.getText().isEmpty()) {
            usernameAlertText.setText("You have to enter you username!");
            return;
        }
        if (passwordTextField.getText().isEmpty()) {
            passwordAlertText.setText("You have to enter your password!");
            return;
        }
        if (!Regex.isUsernameValid(usernameTextField.getText())) {
            usernameAlertText.setText("Your username in invalid!");
            return;
        }
        if (!Regex.isPassValid(passwordTextField.getText())) {
            passwordAlertText.setText("Your password in invalid!");
            return;
        }
        HashMap<String, String> usermap = new HashMap<String, String>();
        usermap.put("username", usernameTextField.getText());
        usermap.put("password", passwordTextField.getText());
        if (!AppController.getInstance().login(usermap)) {
            infoAlertText.setText("Wrong information!");
        } else {
            App.switchScene(event, MainPageController.class.getResource("MainPage.fxml"));
        }
    }
}


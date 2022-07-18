package com.Menu;

import com.Client.*;
import com.MainPage.*;
import com.app.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.*;
import java.net.*;
import java.util.*;

public class SignUpController implements Initializable {

    @FXML
    private TextField emailTextField;

    @FXML
    private Text infoAlertText;

    @FXML
    private Text passwordAlertText;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Text emailAlertText;

    @FXML
    private Text usernameAlertText;

    @FXML
    private TextField usernameTextField;

    @FXML
    void LoginPressed(ActionEvent event) throws IOException {
        App.switchScene(event, LoginController.class.getResource("login.fxml"));
    }

    @FXML
    void continueButtonPressed(ActionEvent event) throws IOException, ClassNotFoundException {
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
        if (emailTextField.getText().isEmpty()) {
            emailAlertText.setText("You have to enter your email!");
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
        if (!Regex.isEmailValid(emailTextField.getText())) {
            emailAlertText.setText("Your email is invalid!");
            return;
        }
        HashMap<String, String> usermap = new HashMap<String, String>();
        usermap.put("username", usernameTextField.getText());
        usermap.put("password", passwordTextField.getText());
        if (!AppController.getInstance().signUp(usermap, emailTextField.getText())) {
            infoAlertText.setText("You've logged in before!");
        } else {
            System.out.println("Entered");
            App.switchScene(event, MainPageController.class.getResource("MainPage.fxml"));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(AppController.getInstance());
    }
}

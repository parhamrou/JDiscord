package com.Profile;

import com.Client.*;
import com.MainPage.*;
import com.Menu.*;
import com.app.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class ProfileController implements Initializable {

    @FXML
    private Circle avatarCircle;

    @FXML
    private Text emailAlertText;

    @FXML
    private Text usernameAlertText;

    @FXML
    private Text phoneAlertText;

    @FXML
    private Button usernameEditButton;

    @FXML
    private Button emailEditButton;

    @FXML
    private Button phoneEditButton;

    @FXML
    private Button changeAvatarButton;

    @FXML
    private Button backButton;

    @FXML
    private Button emailCancelButton;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button logOutButton;

    @FXML
    private Button phoneCancelButton;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Button tryChangeEmailButton;

    @FXML
    private Button tryChangePhoneButton;

    @FXML
    private Button tryChangeUsernameButton;

    @FXML
    private Button usernameCancelButton;

    @FXML
    private Text usernameText;

    @FXML
    private TextField usernameTextField;

    @FXML
    void backButtonPressed(ActionEvent event) {
        App.switchScene(event, MainPageController.class.getResource("MainPage.fxml"));
    }

    @FXML
    void changePasswordPressed(ActionEvent event) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(ProfileController.class.getResource("changePassword.fxml"));
            stage.setTitle("Change Password");
            stage.setScene(new Scene(root, 400, 200));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancelChangeUsername(ActionEvent event) {
        usernameTextField.setDisable(true);
        usernameAlertText.setText("");
        usernameTextField.setText(AppController.getInstance().getUser().getUsername());
        enableButton(usernameEditButton, tryChangeUsernameButton, usernameCancelButton, emailEditButton, phoneEditButton);
    }

    @FXML
    void cancelChangeEmail(ActionEvent event) {
        emailTextField.setDisable(true);
        emailAlertText.setText("");
        emailTextField.setText(AppController.getInstance().getUser().getEmail());
        enableButton(emailEditButton, tryChangeEmailButton, emailCancelButton, usernameEditButton, phoneEditButton);
    }

    @FXML
    void cancelChangePhone() {
        phoneTextField.setDisable(true);
        phoneAlertText.setText("");
        phoneTextField.setText(AppController.getInstance().getUser().getPhoneNumber());
        enableButton(phoneEditButton, tryChangePhoneButton, phoneCancelButton, emailEditButton, usernameEditButton);
    }

    @FXML
    void changeAvatarPressed(ActionEvent event) {

    }

    @FXML
    void changeEmailPressed(ActionEvent event) {
        emailTextField.setDisable(false);
        disableButton(emailEditButton, tryChangeEmailButton, emailCancelButton, usernameEditButton, phoneEditButton);
    }


    @FXML
    void changePhoneNumberPressed(ActionEvent event) {
        phoneTextField.setDisable(false);
        disableButton(phoneEditButton, tryChangePhoneButton, phoneCancelButton, emailEditButton, usernameEditButton);
    }

    @FXML
    void changeUsernamePressed(ActionEvent event) {
        usernameTextField.setDisable(false);
        disableButton(usernameEditButton, tryChangeUsernameButton, usernameCancelButton, emailEditButton, phoneEditButton);
    }

    @FXML
    void logOutButtonPressed(ActionEvent event) {
        AppController.getInstance().setUser(null);
        App.switchScene(event, LoginController.class.getResource("login.fxml"));
    }

    @FXML
    void tryChangeEmail(ActionEvent event) {
        if (emailTextField.getText().isEmpty()) {
            emailAlertText.setText("Enter the new email!");
            return;
        }
        if (!Regex.isEmailValid(emailTextField.getText())) {
            emailAlertText.setText("Email is invalid!");
            return;
        }
        AppController.getInstance().changeEmail(emailTextField.getText());
        emailTextField.setDisable(true);
        emailAlertText.setText("");
        enableButton(emailEditButton, tryChangeEmailButton, emailCancelButton, usernameEditButton, phoneEditButton);
    }

    @FXML
    void tryChangePhone(ActionEvent event) {
        if (phoneTextField.getText().isEmpty()) {
            phoneAlertText.setText("Enter the new number!");
            return;
        }
        if (!Regex.isNumberValid(phoneTextField.getText())) {
            phoneAlertText.setText("number is invalid!");
            return;
        }
        AppController.getInstance().changePhoneNumber(phoneTextField.getText());
        phoneTextField.setDisable(true);
        phoneAlertText.setText("");
        enableButton(phoneEditButton, tryChangePhoneButton, phoneCancelButton, emailEditButton, usernameEditButton);
    }

    @FXML
    void tryChangeUsername(ActionEvent event) {
        if (usernameTextField.getText().isEmpty()) {
            usernameAlertText.setText("Enter the new username!");
            return;
        }
        if (!Regex.isUsernameValid(usernameTextField.getText())) {
            usernameAlertText.setText("Username is invalid!");
            return;
        }
        if (AppController.getInstance().ChangeUsername(usernameTextField.getText())) {
            usernameTextField.setDisable(true);
            usernameText.setText(usernameTextField.getText());
            usernameAlertText.setText("");
            enableButton(usernameEditButton, tryChangeUsernameButton, usernameCancelButton, emailEditButton, phoneEditButton);
        } else {
            usernameAlertText.setText("This username is taken before!");
            usernameTextField.setText(AppController.getInstance().getUser().getUsername());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameTextField.setDisable(true);
        emailTextField.setDisable(true);
        phoneTextField.setDisable(true);
        usernameTextField.setText(AppController.getInstance().getUser().getUsername());
        emailTextField.setText(AppController.getInstance().getUser().getEmail());
        phoneTextField.setText(AppController.getInstance().getUser().getPhoneNumber());
        setCancelButtonPair(tryChangeUsernameButton, usernameCancelButton, true);
        setCancelButtonPair(tryChangeEmailButton, emailCancelButton, true);
        setCancelButtonPair(tryChangePhoneButton, phoneCancelButton, true);
        usernameText.setText(AppController.getInstance().getUser().getUsername());
    }

    private void setButtonCancel(Button button, boolean status) {
        button.setDisable(status);
        button.setVisible(!status);
    }

    private void setCancelButtonPair(Button button1, Button button2, boolean status) {
        button1.setDisable(status);
        button1.setVisible(!status);
        button2.setDisable(status);
        button2.setVisible(!status);
    }


    private void setTwoButtonsDisable(Button button1, Button button2, boolean status) {
        button1.setDisable(status);
        button2.setDisable(status);
    }

    private void enableButton(Button editButton, Button tryChangeButton, Button cancelButton, Button button1, Button button2) {
        setButtonCancel(editButton, false);
        setCancelButtonPair(tryChangeButton, cancelButton, true);
        setTwoButtonsDisable(button1, button2, false);
    }

    private void disableButton(Button editButton, Button tryChangeButton, Button cancelButton, Button button1, Button button2) {
        setButtonCancel(editButton, true);
        setCancelButtonPair(tryChangeButton, cancelButton, false);
        setTwoButtonsDisable(button1, button2, true);
    }

}


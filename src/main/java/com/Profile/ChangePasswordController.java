package com.Profile;

import com.Client.*;
import com.Menu.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

import java.net.*;
import java.util.*;

public class ChangePasswordController implements Initializable {

    @FXML
    private Text alertText;

    @FXML
    private Button button;

    @FXML
    private PasswordField firstTextField;

    @FXML
    private PasswordField secondTextField;

    @FXML
    void buttonPressed(ActionEvent event) {
        alertText.setText("");
        if (firstTextField.getText().isEmpty() || secondTextField.getText().isEmpty()) {
            alertText.setText("You have to fill both fields!");
            return;
        }
        if (!firstTextField.getText().equals(secondTextField.getText())) {
            alertText.setText("Two fields aren't matched!");
            return;
        }
        if (!Regex.isPassValid(firstTextField.getText())) {
            alertText.setText("Your new password is not valid!");
            return;
        }
        AppController.getInstance().changePassword(firstTextField.getText());
        alertText.setStyle("-fx-fill: #89ff1a");
        alertText.setText("Your password is changed!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alertText.setText("");
        alertText.setStyle("-fx-fill: #ff0000");
    }
}

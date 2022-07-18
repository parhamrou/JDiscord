package com.MainPage;

import com.Client.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.*;
import java.nio.file.attribute.*;
import java.util.*;

public class ChangeServerNameController implements Initializable {

    @FXML
    private Text alertText;

    @FXML
    private TextField textField;

    @FXML
    private Button changeButton;

    @FXML
    void changeButtonPressed(ActionEvent event) {
        alertText.setText("");
        if (textField.getText().isEmpty()) {
            alertText.setText("You have to enter the new name!");
            alertText.setStyle("-fx-fill: #ff0000");
            return;
        }
        ServerHandler.getInstance().changeServerName(textField.getText());
        alertText.setStyle("-fx-fill: #89ff1a");
        alertText.setText("The name of the server is changed!");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alertText.setText("");
        alertText.setStyle("-fx-text-fill: #89ff1a");
    }
}


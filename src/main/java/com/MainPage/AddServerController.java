package com.MainPage;

import com.Menu.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.text.*;

import java.net.*;
import java.util.*;

public class AddServerController implements Initializable{
    @FXML
    private Text alertText;

    @FXML
    private Button createButton;

    @FXML
    private TextField serverTextField;

    @FXML
    void createButtonPressed(ActionEvent event) {
        alertText.setText("");
        alertText.setStyle("-fx-fill: #ff0000");
        if (serverTextField.getText().isEmpty()) {
            alertText.setText("You have to enter the name of the server!");
            return;
        }
        if (AppController.getInstance().createServer(serverTextField.getText())) {
            alertText.setStyle("-fx-fill: #89ff1a");
            alertText.setText("The server is created!");
            MainPageController.getInstance().setServers(AppController.getInstance().getServersList());
            return;
        }
        alertText.setText("The name of the server is used before!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alertText.setText("");
        serverTextField.setStyle("-fx-text-fill: #b6b6b2; -fx-background-color:  #3D3D3D");
    }
}

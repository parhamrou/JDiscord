package com.MainPage;

import com.Client.*;
import com.Menu.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.*;
import java.net.*;
import java.util.*;

public class AddChannelController implements Initializable {

    @FXML
    private Text alertText;

    @FXML
    private TextField channelTextField;

    @FXML
    private Button createButton;

    @FXML
    void createButtonPressed(ActionEvent event) throws IOException, ClassNotFoundException {
        alertText.setText("");
        alertText.setStyle("-fx-fill: #ff0000");
        if (channelTextField.getText().isEmpty()) {
            alertText.setText("You have to enter the name of the channel!");
            return;
        }
        if (ServerHandler.getInstance().createChannel(channelTextField.getText())) {
            alertText.setStyle("-fx-fill: #89ff1a");
            alertText.setText("The channel is created!");
            MainPageController.getInstance().setChannels(ServerHandler.getInstance().getChannels());
            return;
        }
        alertText.setText("The name of the channel is used before!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alertText.setText("");
        channelTextField.setStyle("-fx-text-fill: #b6b6b2; -fx-background-color:  #3D3D3D");
    }
}
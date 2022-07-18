package com.MainPage;

import com.Client.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import java.net.*;
import java.util.*;

public class RemoveUserController implements Initializable {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vbox;

    private void addUserCell(String username) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(400);
        anchorPane.setPrefHeight(50);
        anchorPane.setMinHeight(50);
        anchorPane.setStyle("-fx-background-color:  #616161");
        Label label = new Label();
        label.setText(username);
        label.setLayoutX(27);
        label.setLayoutY(9);
        label.setPrefWidth(217);
        label.setPrefHeight(31);
        label.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
        label.setStyle("-fx-text-fill: #b4b4b4; -fx-background-color: #616161");
        Button button = new Button("➖");
        button.setLayoutX(293);
        button.setLayoutY(13);
        button.setPrefWidth(59);
        button.setPrefHeight(26);
        button.setStyle("-fx-text-fill: #b4b4b4; -fx-background-color: #616161");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #797777"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #616161"));
        button.setOnMouseClicked(e -> {
            ServerHandler.getInstance().removeUser(username);
            vbox.getChildren().remove(anchorPane);
        });
        anchorPane.getChildren().addAll(label, button);
        vbox.getChildren().add(anchorPane);
    }

    private void setUsers() {
        for (String username : ServerHandler.getInstance().getUsers()) {
            addUserCell(username);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scrollPane.setFitToWidth(true);
        setUsers();
    }
}

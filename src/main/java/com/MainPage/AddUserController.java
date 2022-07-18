package com.MainPage;

import com.Client.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class AddUserController implements Initializable {

    @FXML
    private VBox usersVBox;

    @FXML
    private ScrollPane scrollPane;

    private void addUserCell(String username) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(400);
        anchorPane.setPrefHeight(63);
        anchorPane.setMinHeight(63);
        anchorPane.setStyle("-fx-background-color:  #616161");
        Label label = new Label();
        label.setText(username);
        label.setStyle("-fx-background-color: 616161; -fx-text-fill: #c4c4c4");
        Font font = Font.font("Verdana", FontWeight.BOLD, 14);
        label.setFont(font);
        label.setLayoutX(38);
        label.setLayoutY(22);
        label.prefHeight(20);
        label.prefWidth(87);
        Button button = new Button("➕");
        button.setPrefWidth(81);
        button.setPrefHeight(26);
        button.setLayoutX(297);
        button.setLayoutY(19);
        button.setStyle("-fx-background-color: #616161; -fx-text-fill: #c4c4c4;");
        button.setFont(font);
        anchorPane.getChildren().addAll(label, button);
        usersVBox.getChildren().add(anchorPane);
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #777777"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #616161; -fx-text-fill: #c4c4c4"));
        button.setOnMouseClicked(e -> {
            try {
                if (ServerHandler.getInstance().addUser(username)) {
                    System.out.println("Now we can add!");
                    usersVBox.getChildren().remove(anchorPane);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scrollPane.setFitToWidth(true);
        try {
            ArrayList<String> users = ServerHandler.getInstance().getNotAddedFriends();
            for (String user : users) {
                addUserCell(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

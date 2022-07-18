package com.MainPage;

import com.Client.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import java.net.*;
import java.util.*;

public class MapRoleController implements Initializable {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vbox;

    @FXML
    private Text guideText;

    private String name;
    private String username;

    private void addRoleCell(String roleName) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(50);
        anchorPane.setMinHeight(50);
        anchorPane.setPrefWidth(400);
        anchorPane.setStyle("-fx-background-color: #616161");
        Text text = new Text(roleName);
        text.setStyle("-fx-fill: #e7e7e7");
        text.setLayoutX(33);
        text.setLayoutY(30);
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        Button button = new Button();
        button.setPrefHeight(Region.USE_COMPUTED_SIZE);
        button.setMinHeight(Region.USE_COMPUTED_SIZE);
        button.setPrefWidth(Region.USE_COMPUTED_SIZE);
        button.setStyle("-fx-background-color:  #616161; -fx-text-fill: #e7e7e7");
        button.setText("✅");
        button.setLayoutX(308);
        button.setLayoutY(12);
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #7c7c7c"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #616161; -fx-text-fill: #e7e7e7"));
        button.setOnMouseClicked(e -> {
            name = roleName;
            vbox.getChildren().clear();
            setUsers();
            guideText.setText("Select the user you want to map the role to: ");
        });
        anchorPane.getChildren().addAll(button, text);
        vbox.getChildren().add(anchorPane);
    }

    private void addUserCell(String username) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(50);
        anchorPane.setMinHeight(50);
        anchorPane.setPrefWidth(400);
        anchorPane.setStyle("-fx-background-color: #616161");
        Text text = new Text(username);
        text.setStyle("-fx-fill: #e7e7e7");
        text.setLayoutX(33);
        text.setLayoutY(30);
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        Button button = new Button();
        button.setPrefHeight(Region.USE_COMPUTED_SIZE);
        button.setMinHeight(Region.USE_COMPUTED_SIZE);
        button.setPrefWidth(Region.USE_COMPUTED_SIZE);
        button.setStyle("-fx-background-color:  #616161; -fx-text-fill: #e7e7e7");
        button.setText("✅");
        button.setLayoutX(308);
        button.setLayoutY(12);
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #7c7c7c"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #616161; -fx-text-fill: #e7e7e7"));
        button.setOnMouseClicked(e -> {
            this.username = username;
            ServerHandler.getInstance().mapRole(name, username);
            vbox.getChildren().clear();
        });
        anchorPane.getChildren().addAll(button, text);
        vbox.getChildren().add(anchorPane);
    }

    private void setRoles() {
        for (String role : ServerHandler.getInstance().getRoleNames()) {
            addRoleCell(role);
        }
    }

    private void setUsers() {
        for (String username : ServerHandler.getInstance().getUsers()) {
            addUserCell(username);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scrollPane.setFitToWidth(true);
        setRoles();
        guideText.setText("Select the role which you want to map to a user: ");
    }
}

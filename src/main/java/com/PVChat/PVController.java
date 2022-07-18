package com.PVChat;

import com.Common.*;
import com.Menu.*;
import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;
import javafx.stage.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class PVController implements Initializable {

    private static PVController instance;

    public static PVController getInstance() {
        return instance;
    }

    public PVController() {
        instance = this;
        System.out.println("Controller is created");
    }

    public static void setInstanceNull() {
        instance = null;
        System.out.println("Now instance is null");
    }

    @FXML
    private TextField chatTextField;

    @FXML
    private VBox chatVBox;

    @FXML
    private Circle imageCircle;

    @FXML
    private Button sendButton;

    @FXML
    private Text usernameText;

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private Button sendFileButton;

    @FXML
    void sendFileButtonPressed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file == null) {
            return;
        }
        try {
            PVHandler.getInstance().serverWriter("#send file", file.getAbsolutePath());
            addMessage(new FileMessage(AppController.getInstance().getUser(), file.getName(), Files.readAllBytes(file.toPath())), true);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void sendButtonPressed(ActionEvent event) throws ClassNotFoundException {
        if (chatTextField.getText().isEmpty()) {
            return;
        }
        PVHandler.getInstance().serverWriter(chatTextField.getText(), null);
        addMessage(new TextMessage(AppController.getInstance().getUser(), chatTextField.getText()), true);
        chatTextField.clear();
    }

    public void addMessage(Message message, boolean isMyMessage) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(64);
        anchorPane.setPrefWidth(508);
        TextField textField = new TextField();
        textField.setEditable(false);
        textField.setPrefHeight(26);
        textField.setPrefWidth(340);
        textField.setLayoutX(14);
        textField.setLayoutY(23);
        Label label = new Label();
        label.setPrefWidth(340);
        label.setPrefHeight(18);
        label.setLayoutX(14);
        label.setLayoutY(10);
        label.setText(" " + message.getUser().getUsername() + "\t" + message.getDate());
        if (isMyMessage) {
            textField.setStyle("-fx-background-color:  #66ff66");
            label.setStyle("-fx-background-color:  #66ff66");
        } else {
            textField.setStyle("-fx-background-color: #4db8ff");
            label.setStyle("-fx-background-color: #4db8ff");
        }
        anchorPane.getChildren().addAll(textField, label);
        if (message instanceof FileMessage) {
            textField.setText("📂 " + message.getText());
            // creating download Button and define its action
            Button downloadButton = new Button();
            downloadButton.setText("⬇");
            if (isMyMessage) {
                downloadButton.setStyle("-fx-background-color:  #5ffd5f; -fx-font-size: 12; -fx-text-fill: black");
                downloadButton.setOnMouseEntered(e -> downloadButton.setStyle("-fx-background-color: #4efc4e"));
                downloadButton.setOnMouseExited(e -> downloadButton.setStyle("-fx-background-color:  #5ffd5f"));
            } else {
                downloadButton.setStyle("-fx-background-color: #48b6fd; -fx-font-size: 12; -fx-text-fill: black");
                downloadButton.setOnMouseEntered(e -> downloadButton.setStyle("-fx-background-color: #3eadfa"));
                downloadButton.setOnMouseExited(e -> downloadButton.setStyle("-fx-background-color:  #48b6fd"));
            }
            downloadButton.setPrefWidth(33);
            downloadButton.setPrefHeight(26);
            downloadButton.setLayoutX(300);
            downloadButton.setLayoutY(19);
            downloadButton.setOnMouseClicked(e -> {
                // clicked on download button
                try {
                    PVHandler.getInstance().serverWriter("#download file", message.getText());
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            });
            anchorPane.getChildren().add(downloadButton);
        } else {
            textField.setText(message.getText());
        }
        anchorPane.setStyle("-fx-background-color:  #616161");
        Platform.runLater(() -> chatVBox.getChildren().add(anchorPane));
    }

    public void setMessages(ArrayList<Message> messages) {
        usernameText.setText(PVHandler.getInstance().getOtherUserUsername());
        for (Message message : messages) {
            if (message.getUser().getUsername().equalsIgnoreCase(AppController.getInstance().getUser().getUsername())) {
                addMessage(message, true);
            } else {
                addMessage(message, false);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatVBox.getChildren().removeAll();
        chatTextField.clear();
        chatScrollPane.setFitToWidth(true);
    }
}


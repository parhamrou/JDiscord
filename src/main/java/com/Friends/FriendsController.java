package com.Friends;

import com.Common.*;
import com.Menu.*;
import com.PVChat.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

public class FriendsController implements Initializable{

    @FXML
    private Button addFriendButton;

    @FXML
    private Button allButton;

    @FXML
    private Button blockedButton;

    @FXML
    private VBox friendsVBox;

    @FXML
    private Button onlineButton;

    @FXML
    private Button pendingButton;

    @FXML
    void addFriendButtonPressed() throws IOException {
        friendsVBox.getChildren().clear();
        AnchorPane newAnchorPane = FXMLLoader.load(FriendsController.class.getResource("addFriend.fxml"));
        friendsVBox.getChildren().add(newAnchorPane);
    }

    @FXML
    void allButtonPressed(ActionEvent event) {
        friendsVBox.getChildren().clear();
        setFriends();
    }

    @FXML
    void blockedButtonPressed(ActionEvent event) {
        friendsVBox.getChildren().clear();
    }

    @FXML
    void mouseEntered(MouseEvent event) {
        ((Button) event.getTarget()).setStyle("-fx-background-color: #616161");
    }

    @FXML
    void mouseExited(MouseEvent event) {
        ((Button) event.getTarget()).setStyle("-fx-background-color: #464646");
    }

    @FXML
    void onlineButtonPressed(ActionEvent event) {
        friendsVBox.getChildren().clear();
    }

    @FXML
    void pendingButtonPressed(ActionEvent event) {
        friendsVBox.getChildren().clear();
        try {
            setFriendshipRequests();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // add friend controller
    @FXML
    private Text alertText;

    @FXML
    private Button sendButton;

    @FXML
    private TextField usernameTextField;

    @FXML
    void sendButtonPressed(ActionEvent event) {
        alertText.setText("");
        if (usernameTextField.getText().isEmpty()) {
            alertText.setText("You have to enter a username!");
            return;
        }
        if (AppController.getInstance().addNewFriend(usernameTextField.getText())) {
            alertText.setStyle("-fx-fill:  #39c239");
            alertText.setText("The request is sent!");
        }
    }


    private void addFriendshipRequestCell(FriendshipRequest friendshipRequest) {
        AnchorPane anchorPane = new AnchorPane();
        Circle circle = new Circle();
        circle.setRadius(18);
        circle.setCenterX(32);
        circle.setCenterY(40);
        if (friendshipRequest.getSenderUser().getAvatar() != null) {
            circle.setFill(new ImagePattern(new Image(new ByteArrayInputStream(friendshipRequest.getSenderUser().getAvatar()))));
        }
        // setting texts
        Text username = new Text(friendshipRequest.getSenderUser().getUsername());
        username.setStyle("-fx-font-size: 14; -fx-text-fill: #c9c4c4; -fx-highlight-text-fill: #c9c4c4");
        username.setX(60);
        username.setY(37);
        username.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        Text text = new Text("Incoming friend request");
        text.setStyle("-fx-font-size: 11; -fx-text-fill: #a1a1a1");
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        text.setX(60);
        text.setY(52);
        // setting accept button
        Button accept = new Button();
        accept.setText("✅");
        accept.setPrefWidth(36);
        accept.setPrefWidth(36);
        accept.setLayoutX(337);
        accept.setLayoutY(23);
        accept.setStyle("-fx-background-color:  #616161");
        accept.setOnMouseEntered(e -> accept.setStyle("-fx-background-color:  #464646"));
        accept.setOnMouseExited(e -> accept.setStyle("-fx-background-color: #616161"));
        accept.setOnMouseClicked(e -> {
            AppController.getInstance().sendRequestAnswer(username.getText(), true);
            friendsVBox.getChildren().remove(anchorPane);
        });
        // setting reject button
        Button reject = new Button();
        reject.setText("❌");
        reject.setPrefWidth(36);
        reject.setPrefWidth(36);
        reject.setLayoutX(444);
        reject.setLayoutY(23);
        reject.setStyle("-fx-background-color:  #616161");
        reject.setOnMouseEntered(e -> reject.setStyle("-fx-background-color:  #464646"));
        reject.setOnMouseExited(e -> reject.setStyle("-fx-background-color: #616161"));
        reject.setOnMouseClicked(e -> {
            AppController.getInstance().sendRequestAnswer(username.getText(), false);
            friendsVBox.getChildren().remove(anchorPane);
        });
        anchorPane.getChildren().addAll(circle, username, text, accept, reject);
        friendsVBox.getChildren().add(anchorPane);
    }

    private void addFriendCell(String username) {
        AnchorPane anchorPane = new AnchorPane();
        Circle circle = new Circle();
        circle.setRadius(18);
        circle.setCenterX(32);
        circle.setCenterY(40);
        Line line = new Line();
        line.setStyle("-fx-background-color: #9e9e9e");
        line.setStartX(-214.80001831054688);
        line.setStartY(-1.2218952178955078E-5);
        line.setEndX(250.80001831054688);
        line.setEndY(-1.2218952178955078E-5);
        Text text = new Text(username);
        text.setStyle("-fx-font-size: 14; -fx-text-fill: #c9c4c4; -fx-highlight-text-fill: #c9c4c4");
        text.setX(60);
        text.setY(37);
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        anchorPane.getChildren().addAll(circle, text, line);
        friendsVBox.getChildren().add(anchorPane);
    }


    private void setFriendshipRequests() throws IOException {
        List<FriendshipRequest> requests = AppController.getInstance().showFriendshipRequests();
        for (FriendshipRequest friendshipRequest : requests) {
            addFriendshipRequestCell(friendshipRequest);
        }
    }

    private void setFriends() {
        friendsVBox.getChildren().clear();
        ArrayList<String> friends = AppController.getInstance().getFriendsList();
        for (String friend : friends) {
            addFriendCell(friend);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //usernameTextField.setStyle("-fx-text-fill: #b6b6b2; -fx-background-color:  #3D3D3D");
    }
}

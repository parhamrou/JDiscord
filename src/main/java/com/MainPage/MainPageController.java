package com.MainPage;

import com.Channel.*;
import com.Friends.*;
import com.Menu.*;
import com.PVChat.*;
import com.Profile.*;
import com.app.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;

import java.io.*;
import java.net.*;
import java.util.*;

import com.Client.*;
import javafx.stage.*;

public class MainPageController implements Initializable {

    private static MainPageController instance;

    public MainPageController() {
        if (instance == null) {
            instance = this;
        }
    }

    public static MainPageController getInstance() {
        return instance;
    }

    @FXML
    private Label title;

    @FXML
    private Button AddPrivateChatButton;

    @FXML
    private Button friendsButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button profileButton;

    @FXML
    private VBox serversVBox;

    @FXML
    private VBox PVChatsVBox;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button refreshButton;

    @FXML
    private ScrollPane serversScrollPane;

    @FXML
    private ScrollPane pvChatsScrollPane;

    @FXML
    void AddPrivateChatButtonPressed(ActionEvent event) {
        AppController.getInstance().cleanChatControllers();
        setAddPvList();
    }

    @FXML
    void refreshButtonPressed(ActionEvent event) {
        refresh();
    }

    private void refresh() {
        AppController.getInstance().cleanChatControllers();
        anchorPane.getChildren().clear();
        setServers(AppController.getInstance().getServersList());
        setPVChats(AppController.getInstance().getPVChatsList());
    }

    private void setAddPvList() {
        PVChatsVBox.getChildren().clear();
        ArrayList<String> friends = AppController.getInstance().getAddPVChatList();
        for (String friend : friends) {
            addPVCell(friend);
        }
    }

    private void addPVCell(String username) {
        AnchorPane newAnchor = new AnchorPane();
        newAnchor.setPrefWidth(313);
        newAnchor.setPrefHeight(80);
        newAnchor.setMinHeight(80);
        Circle circle = new Circle();
        circle.setRadius(18);
        circle.setCenterX(32);
        circle.setCenterY(40);
        Text text = new Text(username);
        text.setStyle("-fx-font-size: 14; -fx-text-fill: #c9c4c4; -fx-highlight-text-fill: #c9c4c4");
        text.setX(60);
        text.setY(37);
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        Button button = new Button();
        button.setText("➕");
        button.setPrefWidth(36);
        button.setPrefWidth(36);
        button.setLayoutX(259);
        button.setLayoutY(23);
        button.setStyle("-fx-background-color:   #3D3D3D");
        newAnchor.getChildren().addAll(button, circle, text);
        // button functions
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color:  #464646"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color:  #3D3D3D"));
        button.setOnMouseClicked(e -> {
            // add button function
            // we have to remove, show the list of the PVs, and go inside the new chat
            AppController.getInstance().cleanChatControllers();
            AppController.getInstance().createPVChat(username);
            PVChatsVBox.getChildren().clear();
            setPVChats(AppController.getInstance().getPVChatsList());
        });
        PVChatsVBox.getChildren().add(newAnchor);
    }

    @FXML
    void friendsButtonPressed(ActionEvent event) throws IOException {
        refresh();
        AnchorPane newAnchorPane = FXMLLoader.load(FriendsController.class.getResource("Friends.fxml"));
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(newAnchorPane);
    }

    @FXML
    void profileButtonPressed(ActionEvent event) {
        refresh();
        App.switchScene(event, ProfileController.class.getResource("profile.fxml"));
    }

    @FXML
    void friendsMouseEntered(MouseEvent event) {

    }

    @FXML
    void homeButtonPressed(ActionEvent event) {
        refresh();
    }

    public void addServerCell(String serverName) {
        AnchorPane newAnchor = new AnchorPane();
        newAnchor.setPrefWidth(78);
        newAnchor.setPrefHeight(50);
        newAnchor.setMinHeight(50);
        newAnchor.setStyle("-fx-background-color:  #3D3D3D");
        Circle circle = new Circle();
        circle.setStyle("-fx-background-color: #4f4e4e; -fx-fill: #4f4e4e");
        circle.setRadius(21);
        circle.setLayoutX(33);
        circle.setLayoutY(25);
        Text text = new Text();
        text.setLayoutX(25);
        text.setLayoutY(30);
        text.setStyle("-fx-fill: #b1b1b1");
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 11));
        text.setText(serverName.substring(0, 2).toUpperCase(Locale.ROOT));
        newAnchor.getChildren().addAll(circle, text);
        serversVBox.getChildren().add(newAnchor);
        newAnchor.setOnMouseEntered(e -> newAnchor.setStyle("-fx-background-color:  #5c42cc"));
        newAnchor.setOnMouseExited(e -> newAnchor.setStyle("-fx-background-color:  #3D3D3D"));
        newAnchor.setOnMouseClicked(e -> {
            title.setText("");
            refresh();
            AddPrivateChatButton.setDisable(true);
            AddPrivateChatButton.setVisible(false);
            AppController.getInstance().enterServer(serverName);
            setChannels(ServerHandler.getInstance().getChannels());
        });
    }

    public void addPVChat(String PVChat) {
        Circle circle = new Circle();
        circle.setRadius(18);
        circle.setCenterX(32);
        circle.setCenterY(40);
        Text text = new Text(PVChat);
        text.setStyle("-fx-fill:  #8a8a8a");
        text.setX(60);
        text.setY(37);
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        AnchorPane newAnchor = new AnchorPane();
        newAnchor.setPrefWidth(313);
        newAnchor.setPrefHeight(80);
        newAnchor.setMinHeight(80);
        newAnchor.getChildren().addAll(text, circle);
        PVChatsVBox.getChildren().add(newAnchor);
        // mouse actions
        newAnchor.setOnMouseEntered(e -> newAnchor.setStyle("-fx-background-color:  #5c42cc"));
        newAnchor.setOnMouseExited(e -> newAnchor.setStyle("-fx-background-color:   #3D3D3D"));
        newAnchor.setOnMouseClicked(e -> {
            // enter pv functions
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                AppController.getInstance().cleanChatControllers();
                AnchorPane newAnchorPane = fxmlLoader.load(PVController.class.getResource("PVChat.fxml"));
                anchorPane.getChildren().clear();
                anchorPane.getChildren().add(newAnchorPane);
                AppController.getInstance().enterPV(PVChat);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void addChannelCell(String channelName) {
        Text text = new Text("#" + channelName);
        text.setStyle("-fx-fill:  #6c6c6c");
        text.setLayoutX(14);
        text.setLayoutY(24);
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        AnchorPane newAnchor = new AnchorPane();
        newAnchor.setPrefWidth(313);
        newAnchor.setPrefHeight(35);
        newAnchor.setMinHeight(30);
        newAnchor.getChildren().addAll(text);
        PVChatsVBox.getChildren().add(newAnchor);
        // mouse actions
        newAnchor.setOnMouseEntered(e -> newAnchor.setStyle("-fx-background-color:  #818181"));
        newAnchor.setOnMouseExited(e -> newAnchor.setStyle("-fx-background-color:   #3D3D3D"));
        newAnchor.setOnMouseClicked(e -> {
            // enter channel function
            makeChannelHandlerNull();
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                AnchorPane newAnchorPane = fxmlLoader.load(ChannelController.class.getResource("Channel.fxml"));
                anchorPane.getChildren().clear();
                anchorPane.getChildren().add(newAnchorPane);
                ServerHandler.getInstance().enterChannel(channelName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void makeChannelHandlerNull() {
        if (com.Channel.ChannelHandler.getInstance() != null) {
            try {
                com.Channel.ChannelHandler.getInstance().exit();
                com.Channel.ChannelHandler.setInstanceNull();
                ChannelController.setInstanceNull();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setServers(ArrayList<String> servers) {
        serversVBox.getChildren().clear();
        createServerButton();
        for (String serverName : servers) {
            addServerCell(serverName);
        }
    }

    public void setChannels(ArrayList<String> channels) {
        PVChatsVBox.getChildren().clear();
        serverButtons();
        title.setText("");
        for (String channelName : channels) {
            addChannelCell(channelName);
        }
    }

    private void createServerButton() {
        AnchorPane newAnchor = new AnchorPane();
        newAnchor.setPrefWidth(78);
        newAnchor.setPrefHeight(50);
        newAnchor.setMinHeight(50);
        newAnchor.setStyle("-fx-background-color:  #3D3D3D");
        Circle circle = new Circle();
        circle.setStyle("-fx-background-color: #4f4e4e; -fx-fill: #1f1fde");
        circle.setRadius(21);
        circle.setLayoutX(33);
        circle.setLayoutY(25);
        Text text = new Text();
        text.setLayoutX(28);
        text.setLayoutY(30);
        text.setStyle("-fx-text-fill: #b1b1b1; -fx-fill: #b1b1b1");
        text.setText("➕");
        newAnchor.getChildren().addAll(circle, text);
        serversVBox.getChildren().add(newAnchor);
        newAnchor.setOnMouseEntered(e -> newAnchor.setStyle("-fx-background-color:  #5c42cc"));
        newAnchor.setOnMouseExited(e -> newAnchor.setStyle("-fx-background-color:  #3D3D3D"));
        newAnchor.setOnMouseClicked(e -> {
            try {
                refresh();
                Parent root = FXMLLoader.load(MainPageController.class.getResource("createServer.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Create server");
                stage.setScene(new Scene(root, 500, 150));
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void serverButtons() {
        AnchorPane newAnchor = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: #616161");
        newAnchor.setPrefHeight(115);
        newAnchor.setPrefWidth(313);
        // line setting
        Line line = new Line();
        line.setStrokeWidth(1);
        line.setStrokeType(StrokeType.CENTERED);
        line.setStyle("-fx-fill: #c7c7c7");
        line.setStartX(-100);
        line.setEndX(100);
        line.setStartY(0);
        line.setEndY(0);
        line.setLayoutX(156);
        line.setLayoutY(115);
        // add channel button setting
        Button addChannel = new Button();
        buttonSetting(addChannel, 14, 10, "Add Channel");
        addChannel.setOnMouseClicked(e -> {
            try {
                makeChannelHandlerNull();
                if (ServerHandler.getInstance().canCreateChannel()) {
                    openNewWindow("createChannel", 500, 150, "Create Channel");
                } else {
                    showError();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        // add user button setting
        Button addUser = new Button();
        buttonSetting(addUser, 97, 10, "Add User");
        addUser.setOnMouseClicked(e -> {
            try {
                makeChannelHandlerNull();
                openNewWindow("addUser", 400, 400, "Add User");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        // add role map
        Button addRole = new Button();
        buttonSetting(addRole, 167, 10, "Add Role");
        addRole.setOnMouseClicked(e -> {
            try {
                makeChannelHandlerNull();
                if (ServerHandler.getInstance().canCreateRole()) {
                    openNewWindow("addRole", 500, 500, "Add Role");
                } else {
                    showError();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Button mapRole = new Button();
        buttonSetting(mapRole, 237, 10, "Map Role");
        mapRole.setOnMouseClicked(e -> {
            makeChannelHandlerNull();
            try {
                makeChannelHandlerNull();
                if (ServerHandler.getInstance().canMapRole()) {
                    openNewWindow("mapRole", 400, 300, "Map Role");
                } else {
                    showError();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        // remove channel button
        Button removeChannel = new Button();
        buttonSetting(removeChannel, 14, 47, "Remove Channel");
        removeChannel.setOnMouseClicked(e -> {
            try {
                makeChannelHandlerNull();
                if (ServerHandler.getInstance().canRemoveChannel()) {
                    openNewWindow("removeChannel", 400, 400, "Remove Channel");
                } else {
                    showError();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        // remove user button
        Button removeUser = new Button();
        buttonSetting(removeUser, 115, 47, "Remove User");
        removeUser.setOnMouseClicked(e -> {
            try {
                makeChannelHandlerNull();
                if (ServerHandler.getInstance().canRemoveUser()) {
                    openNewWindow("removeUser", 400, 400, "Remove User");
                } else {
                    showError();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        // change server name button
        Button changeName = new Button();
        buttonSetting(changeName, 195, 47, "Change Name");
        changeName.setOnMouseClicked(e -> {
            try {
                makeChannelHandlerNull();
                if (ServerHandler.getInstance().canChangeName()) {
                    openNewWindow("changeServerName", 400, 100, "Change Name");
                } else {
                    showError();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Button removeServer = new Button();
        removeServer.setPrefHeight(26);
        removeServer.setPrefWidth(201);
        removeServer.setLayoutX(56);
        removeServer.setLayoutY(81);
        removeServer.setStyle("-fx-background-color: #a11616; -fx-text-fill: #b4b4b4");
        removeServer.setFont(Font.font("Verdana", FontWeight.BOLD, 9));
        removeServer.setText("Remove Server");
        removeServer.setOnMouseEntered(e -> removeServer.setStyle("-fx-background-color: #ff2121"));
        removeServer.setOnMouseExited(e -> removeServer.setStyle("-fx-background-color: #a11616; -fx-text-fill: #b4b4b4"));
        removeServer.setOnMouseClicked(e -> {
            try {
                makeChannelHandlerNull();
                if (ServerHandler.getInstance().canRemoveServer()) {
                    ServerHandler.getInstance().removeServer();
                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(MainPageController.class.getResource("message.fxml"));
                    stage.setTitle("Message");
                    stage.setScene(new Scene(root, 400, 100));
                    stage.show();
                } else {
                    showError();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        newAnchor.getChildren().addAll(removeServer, addChannel, removeChannel, changeName, addUser, removeUser, addRole, mapRole, line);
        PVChatsVBox.getChildren().add(newAnchor);
    }

    private void buttonSetting(Button button, int x, int y, String text) {
        Font font = Font.font("Verdana", FontWeight.BOLD, 9);
        button.setStyle("-fx-background-color:  #3D3D3D; -fx-text-fill: #9e9e9e");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #616161"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color:  #3D3D3D; -fx-text-fill: #9e9e9e"));
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setText(text);
        button.setFont(font);
        button.setPrefWidth(Control.USE_COMPUTED_SIZE);
        button.setPrefHeight(Control.USE_COMPUTED_SIZE);
        button.setMinHeight(Control.USE_COMPUTED_SIZE);
        button.setMaxHeight(Control.USE_COMPUTED_SIZE);
        button.setMinWidth(Control.USE_COMPUTED_SIZE);
        button.setMaxWidth(Control.USE_COMPUTED_SIZE);
    }


    private void showError() {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(MainPageController.class.getResource("Error.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Error");
        stage.setScene(new Scene(root, 500, 100));
        stage.show();
    }

    private void openNewWindow(String resource, int width, int height, String windowTitle) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(MainPageController.class.getResource(resource + ".fxml"));
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root, width, height));
        stage.show();
    }


    private void setPVChats(ArrayList<String> PVChats) {
        PVChatsVBox.getChildren().clear();
        title.setText("DIRECT CHATS");
        AddPrivateChatButton.setDisable(false);
        AddPrivateChatButton.setVisible(true);
        for (String PVChat : PVChats) {
            addPVChat(PVChat);
        }
    }

    private void buttonsSetting() {
        // friends button setting
        friendsButton.setOnMouseEntered(e -> friendsButton.setStyle("-fx-background-color:  #616161"));
        friendsButton.setOnMouseExited(e -> friendsButton.setStyle("-fx-background-color:  #4F4E4E"));
        // add private chat setting
        AddPrivateChatButton.setOnMouseEntered(e -> AddPrivateChatButton.setStyle("-fx-background-color:  #616161"));
        AddPrivateChatButton.setOnMouseExited(e -> AddPrivateChatButton.setStyle("-fx-background-color:  #4F4E4E"));
        refreshButton.setOnMouseEntered(e -> refreshButton.setStyle("-fx-background-color:  #616161"));
        refreshButton.setOnMouseExited(e -> refreshButton.setStyle("-fx-background-color:  #4F4E4E"));
        profileButton.setText("🚹");
        profileButton.setOnMouseEntered(e -> profileButton.setStyle("-fx-background-color:  #575757"));
        profileButton.setOnMouseExited(e -> profileButton.setStyle("-fx-background-color:  #4F4E4E"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonsSetting();
        setServers(AppController.getInstance().getServersList());
        setPVChats(AppController.getInstance().getPVChatsList());
        AddPrivateChatButton.setOnMouseEntered(e -> AddPrivateChatButton.setStyle("-fx-background-color:   #999999"));
        AddPrivateChatButton.setOnMouseExited(e -> AddPrivateChatButton.setStyle("-fx-background-color: #616161"));
        serversScrollPane.setFitToWidth(true);
        pvChatsScrollPane.setFitToWidth(true);
    }
}

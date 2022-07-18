package com.app;

import com.MainPage.*;
import com.Menu.*;
import com.Profile.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;

public class App extends Application {

    private static Parent root;
    private static Stage stage;
    private static Scene scene;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        root = FXMLLoader.load(LoginController.class.getResource("signUp.fxml"));
        scene = new Scene(root);
        stage = primaryStage;
        stage.setScene(scene);
        stage.show();
    }

    public static void switchScene(ActionEvent event, URL url)  {
        try {
            root = FXMLLoader.load(url);
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

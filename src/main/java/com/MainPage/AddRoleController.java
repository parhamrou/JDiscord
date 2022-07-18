package com.MainPage;

import com.Client.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.*;
import java.util.*;

public class AddRoleController implements Initializable {

    @FXML
    private Text alertText;

    @FXML
    private CheckBox banFromUser;

    @FXML
    private CheckBox changeName;

    @FXML
    private CheckBox createChannel;

    @FXML
    private Button createRoleButton;

    @FXML
    private CheckBox limitUser;

    @FXML
    private CheckBox removeChannel;

    @FXML
    private CheckBox removeUser;

    @FXML
    private CheckBox pinMessage;
    @FXML
    private TextField roleNameTextField;

    @FXML
    private CheckBox seeHistory;

    @FXML
    void createRoleButtonPressed(ActionEvent event) {
        alertText.setText("");
        alertText.setStyle("-fx-fill: #ff0000");
        if (roleNameTextField.getText().isEmpty()) {
            alertText.setText("You have to enter the name of the role!");
            return;
        }
        if ((!createChannel.isSelected()) && (!removeChannel.isSelected()) && (!seeHistory.isSelected())
           && (!removeUser.isSelected()) && (!limitUser.isSelected()) && (!changeName.isSelected()) &&
            (!banFromUser.isSelected()) && (!pinMessage.isSelected())) {
            alertText.setText("At least You have to check one item!");
            return;
        }

        boolean[] abilities = new boolean[8];
        abilities[0] = createChannel.isSelected();
        abilities[1] = removeChannel.isSelected();
        abilities[2] = removeUser.isSelected();
        abilities[3] = limitUser.isSelected();
        abilities[4] = banFromUser.isSelected();
        abilities[5] = changeName.isSelected();
        abilities[6] = seeHistory.isSelected();
        abilities[7] = pinMessage.isSelected();
        ServerHandler.getInstance().createRole(roleNameTextField.getText(), abilities);
        alertText.setStyle("-fx-fill: #89ff1a");
        alertText.setText("The role is created!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alertText.setText("");
        roleNameTextField.setStyle("-fx-text-fill: #b6b6b2; -fx-background-color:  #3D3D3D");
    }
}

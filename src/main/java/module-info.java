module com {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.Menu to javafx.fxml;
    opens com.MainPage to javafx.fxml;
    opens com.Friends to javafx.fxml;
    opens com.PVChat to javafx.fxml;
    opens com.Client to javafx.fxml;
    opens com.Channel to javafx.fxml;
    opens com.Profile to javafx.fxml;
    exports com.app;
}
package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class AppointmentSystem extends Application{


    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ViewController/LoginScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        database.startConnection();
        launch(args);
        database.closeConnection();
    }
}



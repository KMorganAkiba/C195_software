package ViewController;

import Models.Appointment;
import Models.AppointmentDatabase;
import Models.UserDatabase;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginScreen implements Initializable {
    public Label UsernameLabel;
    public Label PasswordLabel;
    public TextField UsernameTxtbox;
    public TextField PasswordTxtbox;
    public Button SubmitButton;
    public Label LanguageDeclerationLabel;
    public Label SystemTitleText;
    private String alerttext;


    public void SubmitButton(MouseEvent mouseEvent) throws IOException {
        String userName = UsernameTxtbox.getText();
        String password = PasswordTxtbox.getText();
        Boolean user  = UserDatabase.login(userName, password);
        if(user) {
            Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreenWCalendar.fxml"));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainScreenStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            mainScreenStage.setScene(mainScreenScene);
            mainScreenStage.show();
            Appointment appointment = AppointmentDatabase.appointmentIn15Min();
            if (appointment != null){
                String notification = String.format("You have a %s appointment in less than 15 minutes.",appointment.getAppointmentDescription());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Appointment Notification");
                alert.setContentText(notification);
                alert.showAndWait();
            }

        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(alerttext);
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Locale locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("LoginProperties/login", locale);
        UsernameLabel.setText(rb.getString("username"));
        PasswordLabel.setText(rb.getString("password"));
        SubmitButton.setText(rb.getString("submit"));
        alerttext = rb.getString("alerttext");
        LanguageDeclerationLabel.setText(rb.getString("language"));
        SystemTitleText.setText(rb.getString("title"));


    }
}

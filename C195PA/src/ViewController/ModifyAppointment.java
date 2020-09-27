package ViewController;

import Models.Appointment;
import Models.AppointmentDatabase;
import Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyAppointment implements Initializable {
    public static Appointment modAptCust = null;
    public DatePicker modCalenderDate;
    public ComboBox modAptTimeSelection;
    public ComboBox modAgentSelection;
    public TextField modLocationTxtBox;
    public ComboBox modTypeOfAppointment;
    public TextField modAptID;

    private ObservableList<String> moderror = FXCollections.observableArrayList();
    private ObservableList<String> modAgent = FXCollections.observableArrayList("Steve", "Colt", "Shinji");
    private ObservableList<String> modAptTimes = FXCollections.observableArrayList("09:00 AM","10:00 AM","11:00 AM","12:00 PM","1:00 PM","2:00 PM","3:00 PM","4:00 PM");
    private ObservableList<String> modTypApt = FXCollections.observableArrayList("Free Consultation", "System Requirements Meeting", "Acquisitions","Budgetary Meetings");

    public boolean ModifyAppointmentButton(MouseEvent mouseEvent) throws IOException {
        moderror.clear();
        int aptContact = modAgentSelection.getSelectionModel().getSelectedIndex();
        String aptType = modTypeOfAppointment.getSelectionModel().getSelectedItem().toString();
        int aptTime = modAptTimeSelection.getSelectionModel().getSelectedIndex();
        LocalDate ld = modCalenderDate.getValue();
        if(!validateContact(aptContact)||!validateTime(aptTime)||!validateDate(ld)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("All fields are required.");
                alert.show();
            return false;
        }
        if(AppointmentDatabase.overlappingAppointment(-1, modLocationTxtBox.getText(), ld.toString(), modAptTimes.get(aptTime))) {
            moderror.add("Overlapping Appointments");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.show();
            alert.setHeaderText("An appointment for this agent already exists at this time.");
            return false;
        }
        if(AppointmentDatabase.updateAppointment(modAptCust.getAppointmentID(), aptType, modAgent.get(aptContact), modLocationTxtBox.getText(), ld.toString(), modAptTimes.get(aptTime))) {
            Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreenWCalendar.fxml"));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainScreenStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            mainScreenStage.setScene(mainScreenScene);
            mainScreenStage.show();
            return true;
        } else {
            moderror.add("Database Error");
            return false;
        }
    }

    public boolean validateContact(int aptContact) {
        if(aptContact == -1) {
            moderror.add("A Contact must be selected");
            return false;
        } else {
            return true;
        }
    }

    public boolean validateType(int aptType) {
        if(aptType == -1) {
            moderror.add("An Appointment Type must be selected");
            return false;
        } else {
            return true;
        }
    }

    public boolean validateTime(int aptTime) {
        if(aptTime == -1) {
            moderror.add("An Appointment Time must be selected");
            return false;
        } else {
            return true;
        }
    }

    public boolean validateDate(LocalDate aptDate) {
        if(aptDate == null) {
            moderror.add("An Appointment Date must be selected");
            return false;
        } else {
            return true;
        }
    }

    public String displayErrors(){
        String s = "";
        if(moderror.size() > 0) {
            for(String err : moderror) {
                s = s.concat(err);
            }
            return s;
        } else {
            s = "Database Error";
            return s;
        }
    }

    public void CancelModifyAppointmentButton(MouseEvent mouseEvent) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Do you wish to exit and return to the main screen!");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreenWCalendar.fxml"));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainScreenStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            mainScreenStage.setScene(mainScreenScene);
            mainScreenStage.show();
        }
        else{
            return;
        }
    }

    public void setmodLocation() {
        String loc = modAgentSelection.getSelectionModel().getSelectedItem().toString();
        if (loc.equals("Steve")) {
            modLocationTxtBox.setText("New York");
        } else if (loc.equals("Colt")) {
            modLocationTxtBox.setText("London");
        } else if (loc.equals("Shinji")) {
            modLocationTxtBox.setText("Phoenix");
        }
    }
    public void populateAptTextBoxes(Appointment appointment){
        int id = appointment.getAppointmentID();
        String iD = Integer.toString(id);
        modAptID.setText(iD);
        modCalenderDate.setValue(appointment.getDateOnly());
        modAptTimeSelection.getSelectionModel().select(appointment.getTimeOnly());
        modAgentSelection.getSelectionModel().select(appointment.getAppointmentContact());
        setmodLocation();
        modTypeOfAppointment.getSelectionModel().select(appointment.getAppointmentDescription());


    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modAgentSelection.setItems(modAgent);
        modAptTimeSelection.setItems(modAptTimes);
        modTypeOfAppointment.setItems(modTypApt);
        populateAptTextBoxes(modAptCust);

        modCalenderDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(
                        empty ||
                                date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                                date.getDayOfWeek() == DayOfWeek.SUNDAY ||
                                date.isBefore(LocalDate.now()));
                if(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY || date.isBefore(LocalDate.now())) {
                    setStyle("-fx-background-color: #8A2BE2;");
                }
            }
        });

    }
}

package ViewController;

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


public class AddAppointment implements Initializable{
    public TextField customerNametxtbox;
    public DatePicker calenderDate;
    public ComboBox aptTimeSelection;
    public ComboBox agentSelection;
    public TextField locationTxtBox;
    public Button cancelAddAppointmentButton;
    public Button addAppointmentButton;
    public ComboBox typeOfAppointment;
    public static Customer AptCust = null;
    private ObservableList<String> error = FXCollections.observableArrayList();
    private ObservableList<String> agent = FXCollections.observableArrayList("Steve", "Colt", "Shinji");
    private ObservableList<String> aptTimes = FXCollections.observableArrayList("09:00 AM","10:00 AM","11:00 AM","12:00 PM","13:00 PM","14:00 PM","15:00 PM","16:00 PM");
    private ObservableList<String> typApt = FXCollections.observableArrayList("Free Consultation", "System Requirements Meeting", "Acquisitions","Budgetary Meetings");

    public boolean AddAppointmentButton(MouseEvent mouseEvent) throws IOException {
        error.clear();
        int aptContact = agentSelection.getSelectionModel().getSelectedIndex();
        String aptType = typeOfAppointment.getSelectionModel().getSelectedItem().toString();
        int aptTime = aptTimeSelection.getSelectionModel().getSelectedIndex();
        LocalDate ld = calenderDate.getValue();
        if(aptContact== -1||aptType.isEmpty()||aptTime == -1||ld == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("All Fields must be populated.");
            alert.showAndWait();
        }
        if(AppointmentDatabase.overlappingAppointment(-1, locationTxtBox.getText(), ld.toString(), aptTimes.get(aptTime))) {
            error.add("Overlapping Appointments");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.show();
                alert.setHeaderText("An appointment for this agent already exists at this time.");
            return false;
        }
        if(AppointmentDatabase.addAppointment(AptCust.getCustomerID(), aptType, agent.get(aptContact), locationTxtBox.getText(), ld.toString(), aptTimes.get(aptTime))) {
            Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreenWCalendar.fxml"));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainScreenStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            mainScreenStage.setScene(mainScreenScene);
            mainScreenStage.show();
            return true;
        } else {
            error.add("Database Error");
            return false;
        }
    }

    public boolean validateContact(int aptContact) {
        if(aptContact == -1) {
            error.add("A Contact must be selected");
            return false;
        } else {
            return true;
        }
    }

    public boolean validateType(int aptType) {
        if(aptType == -1) {
            error.add("An Appointment Type must be selected");
            return false;
        } else {
            return true;
        }
    }

    public boolean validateTime(int aptTime) {
        if(aptTime == -1) {
            error.add("An Appointment Time must be selected");
            return false;
        } else {
            return true;
        }
    }

    public boolean validateDate(LocalDate aptDate) {
        if(aptDate == null) {
            error.add("An Appointment Date must be selected");
            return false;
        } else {
            return true;
        }
    }

    public String displayErrors(){
        String s = "";
        if(error.size() > 0) {
            for(String err : error) {
                s = s.concat(err);
            }
            return s;
        } else {
            s = "Database Error";
            return s;
        }
    }

    public void CancelAddAppointmentButton(MouseEvent mouseEvent) throws IOException {
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

    public void populateCustomerName(Customer customer){
        customerNametxtbox.setText(customer.getCustomerName());
        int custID = customer.getCustomerID();

    }

    public void setLocation(ActionEvent actionEvent) {
        String loc = agentSelection.getSelectionModel().getSelectedItem().toString();
        if(loc.equals("Steve")){
            locationTxtBox.setText("New York");
        }
        else if(loc.equals("Colt")){
            locationTxtBox.setText("London");
        }
        else if(loc.equals("Shinji")){
            locationTxtBox.setText("Phoenix");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        agentSelection.setItems(agent);
        aptTimeSelection.setItems(aptTimes);
        typeOfAppointment.setItems(typApt);
        populateCustomerName(AptCust);

        calenderDate.setDayCellFactory(picker -> new DateCell() {
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

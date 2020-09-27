package ViewController;

import Models.Appointment;
import Models.AppointmentDatabase;
import Models.Customer;
import Models.CustomerDatabase;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class MainScreenWCalendar implements Initializable {
    public TableView <Appointment> AppointmentsTable;
    public Button AddCustomerButton;
    public Button ModifyCustomerButton;
    public Button AddAppointmentButton;
    public Button ModifyAppointmentButton;
    public Button deleteCustomerBtn;
    public Button deleteAppointmentBtn;
    public Button reportsButton;
    public TableView<Customer> CustomerTableView;
    public TableColumn<Customer, Integer> CustomerID;
    public TableColumn<Customer, String> CustomerName;
    public TableColumn <Appointment, Integer> aptIDColumn;
    public TableColumn <Appointment, Integer>CustomerIdColumn;
    public TableColumn <Appointment,String> descriptionColumn;
    public TableColumn <Appointment, String> startTimeColumn;
    public TableColumn <Appointment, String> endTimeColumn;
    public Button monthAptButton;
    public Button weekAptButton;
    public Button showAllAppointments;
    public TableColumn customerAddressIDColumn;
    private Customer selectionCustomer;
    private Appointment selectionAppointment;


    public void AddCustomerButton(MouseEvent mouseEvent) throws IOException {
        Parent addScreenParent = FXMLLoader.load(getClass().getResource("AddCustomer.fxml"));
        Scene addScreenScene = new Scene(addScreenParent);
        Stage addScreenStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        addScreenStage.setScene(addScreenScene);
        addScreenStage.show();
    }

    public void ModifyCustomerButton(MouseEvent mouseEvent) throws IOException{
        Customer cust = CustomerTableView.getSelectionModel().getSelectedItem();
        if (cust == null)
            return;
        ModifyCustomer.modCust = cust;
        Parent MCScreenParent = FXMLLoader.load(getClass().getResource("ModifyCustomer.fxml"));
        Scene MCCScreenScene = new Scene(MCScreenParent);
        Stage MCScreenStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        MCScreenStage.setScene(MCCScreenScene);
        MCScreenStage.show();
    }

    public void AddAppointmentButton(MouseEvent mouseEvent) throws IOException{
        Customer custA = CustomerTableView.getSelectionModel().getSelectedItem();
        if (custA == null)
            return;
        AddAppointment.AptCust = custA;

        Parent addAptScreenParent = FXMLLoader.load(getClass().getResource("AddAppointment.fxml"));
        Scene addAptScreenScene = new Scene(addAptScreenParent);
        Stage addAptScreenStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        addAptScreenStage.setScene(addAptScreenScene);
        addAptScreenStage.show();
    }

    public void ModifyAppointmentButton(MouseEvent mouseEvent) throws IOException{
        Appointment apt = AppointmentsTable.getSelectionModel().getSelectedItem();
        if (apt == null)
            return;
        ModifyAppointment.modAptCust=apt;
        Parent modAptScreenParent = FXMLLoader.load(getClass().getResource("ModifyAppointment.fxml"));
        Scene modAptScreenScene = new Scene(modAptScreenParent);
        Stage modAptScreenStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        modAptScreenStage.setScene(modAptScreenScene);
        modAptScreenStage.show();
    }

    public void ExitSystemButton(MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("You are about to logout of the Appointment system");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Parent loginScreenParent = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
            Scene loginScreenScene = new Scene(loginScreenParent);
            Stage loginScreenStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
            loginScreenStage.setScene(loginScreenScene);
            loginScreenStage.show();
        }
        else{
            return;
        }
    }

    public void deleteCustomerBtn(MouseEvent mouseEvent) {
        if(CustomerTableView.getSelectionModel().getSelectedItem() != null) {
            selectionCustomer = CustomerTableView.getSelectionModel().getSelectedItem();
        } else {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete Customer Record");
        alert.setContentText("Delete Customer: " + selectionCustomer.getCustomerName() + " ?");
        //Verifies response from the user that they want to delete the selected Customer, than calls the method
        //and handles the parameters to control the delete.
        alert.showAndWait().ifPresent((response -> {
            if(response == ButtonType.OK) {
                CustomerDatabase.deleteCustomer(selectionCustomer.getCustomerID(),selectionCustomer.getAddressID());
                CustomerTableView.setItems(CustomerDatabase.getAllCustomers());
            }
        }));
    }

    public void deleteAppointmentBtn(MouseEvent mouseEvent) {
        if(AppointmentsTable.getSelectionModel().getSelectedItem() != null) {
            selectionAppointment = AppointmentsTable.getSelectionModel().getSelectedItem();
        } else {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete Appointment Record");
        alert.setContentText("Do you wish to delete the " +selectionAppointment.getAppointmentDescription()+ " appointment with " +selectionAppointment.getAppointmentContact()+ " ?");
        //Verifies response from the user that they want to delete the selected Appointment, than calls the method
        //and handles the parameter to control the delete.
        alert.showAndWait().ifPresent((response -> {
            if(response == ButtonType.OK) {
                AppointmentDatabase.deleteAppointment(selectionAppointment.getAppointmentID());
                setAppointmentsTable();
            }
        }));
    }

    public void reportsButton(MouseEvent mouseEvent) throws IOException {
        Parent modAptScreenParent = FXMLLoader.load(getClass().getResource("Reports.fxml"));
        Scene modAptScreenScene = new Scene(modAptScreenParent);
        Stage modAptScreenStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        modAptScreenStage.setScene(modAptScreenScene);
        modAptScreenStage.show();
    }

    public void setAppointmentsTable(){
        AppointmentsTable.setItems(AppointmentDatabase.getAllAppointments());
        aptIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        CustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerID"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        CustomerTableView.setItems(CustomerDatabase.getAllCustomers());
        CustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        CustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressIDColumn.setCellValueFactory(new PropertyValueFactory<>("addressID"));
        setAppointmentsTable();
    }

    public void monthAptButton(MouseEvent mouseEvent) {
        AppointmentsTable.setItems(AppointmentDatabase.getMonthlyAppointments());
        aptIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        CustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerID"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
    }

    public void weekAptButton(MouseEvent mouseEvent) {
        AppointmentsTable.setItems(AppointmentDatabase.getWeeklyAppointments());
        aptIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        CustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerID"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
    }

    public void showAllAppointments(MouseEvent mouseEvent) {
        AppointmentsTable.setItems(AppointmentDatabase.getAllAppointments());
        aptIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        CustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerID"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
    }
}

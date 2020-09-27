package ViewController;

import Models.Customer;
import Models.CustomerDatabase;
import com.sun.org.apache.xpath.internal.operations.Mod;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyCustomer implements Initializable {
    public TextField ModCustomerIDTxtbox;
    public TextField ModCustomerNameTxtBox;
    public TextField ModCustomerAddressTxtbox;
    public TextField ModCustomerZipTxtbox;
    public TextField ModCustomerPhoneTxtbox;
    public TextField ModCustomerCountryTxtbox;
    public ComboBox ModCustomerCityBox;
    public TextField addressIDTextbox;
    private ChoiceBox<Object> CustomerCity;
    public static Customer modCust = null;

    private ObservableList<String> city = FXCollections.observableArrayList(
            "Chicago","Los Angeles","Miami","Atlanta","Nashville","Tokyo","Ebisu","Ginza","Osaka"
    );

    public void ModifyCustomerSaveButton(MouseEvent mouseEvent) throws IOException {
        String customerID = ModCustomerIDTxtbox.getText();
        int id = Integer.parseInt(customerID);
        String addressID = addressIDTextbox.getText();
        int addressId = Integer.parseInt(addressID);
        String customerName = ModCustomerNameTxtBox.getText();
        String customerAddress = ModCustomerAddressTxtbox.getText();
        int customerCity = ModCustomerCityBox.getSelectionModel().getSelectedIndex() + 1;
        String customerZip = ModCustomerZipTxtbox.getText();
        String customerPhone = ModCustomerPhoneTxtbox.getText();
        String customerCountry = ModCustomerCountryTxtbox.getText();
        if(customerName.isEmpty()||customerAddress.isEmpty()||customerZip.isEmpty()||customerPhone.isEmpty()||customerCountry.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("All Fields must be populated.");
            alert.showAndWait();
        }
        else {
            CustomerDatabase.updateCustomer(id, addressId, customerName, customerAddress, customerCity, customerZip, customerPhone);
            Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreenWCalendar.fxml"));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainScreenStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            mainScreenStage.setScene(mainScreenScene);
            mainScreenStage.show();
        }
    }

    public void CancelModifyCustomerButton(MouseEvent mouseEvent) throws IOException{
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

    public void populateTextBoxes(Customer customer){
        int id = customer.getCustomerID();
        String iD = Integer.toString(id);
        int aID = customer.getAddressID();
        String adID = Integer.toString(aID);
        ModCustomerIDTxtbox.setText(iD);
        ModCustomerNameTxtBox.setText(customer.getCustomerName());
        ModCustomerAddressTxtbox.setText(customer.getCustomerAddress());
        ModCustomerCityBox.getSelectionModel().select(customer.getCustomerCity());
        setModCustomerCountryTxtbox();
        ModCustomerZipTxtbox.setText(customer.getCustomerZip());
        ModCustomerPhoneTxtbox.setText(customer.getCustomerPhone());
        addressIDTextbox.setText(adID);


    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        ModCustomerCityBox.setItems(city);
        populateTextBoxes(modCust);
    }
    public void setModCustomerCountryTxtbox() {

        String CurrentCity = ModCustomerCityBox.getSelectionModel().getSelectedItem().toString();
        if(CurrentCity.equals("Tokyo")||CurrentCity.equals("Ebisu")||CurrentCity.equals("Ginza")||CurrentCity.equals("Osaka")) {
            ModCustomerCountryTxtbox.setText("Japan");
        } else {
            ModCustomerCountryTxtbox.setText("United States");
        }
    }
}

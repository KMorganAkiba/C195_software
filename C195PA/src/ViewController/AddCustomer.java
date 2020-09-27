package ViewController;

import Models.CustomerDatabase;
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

public class AddCustomer implements Initializable {
    public Button AddCustomerButton;
    public Button CancelCustomerAddButton;
    public Label CustomerIDLabel;
    public Label CustomerNameLabel;
    public Label CustomerAddressLabel;
    public Label CustomerCityLabel;
    public Label CustomerZipLabel;
    public Label CustomerPhoneLabel;
    public TextField CustomerIDTxtbox;
    public TextField CustomerNameTxtbox;
    public TextField CustomerAddressTxtbox;
    public TextField CustomerZipTxtbox;
    public TextField CustomerPhoneTxtbox;
    public ComboBox<String> CustomerCity;
    public TextField CustomerCountryTxtbox;

    private ObservableList<String> city = FXCollections.observableArrayList(
            "Chicago","Los Angeles","Miami","Atlanta","Nashville","Tokyo","Ebisu","Ginza","Osaka"
    );

    public void AddCustomerButton(MouseEvent mouseEvent) throws IOException {
        String customerName = CustomerNameTxtbox.getText();
        String customerAddress = CustomerAddressTxtbox.getText();
        int customerCity = CustomerCity.getSelectionModel().getSelectedIndex() + 1;
        String customerZip = CustomerZipTxtbox.getText();
        String customerPhone = CustomerPhoneTxtbox.getText();
        String customerCountry = CustomerCountryTxtbox.getText();
        if(customerName.isEmpty()||customerAddress.isEmpty()||customerZip.isEmpty()||customerPhone.isEmpty()||customerCountry.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("All Fields must be populated.");
            alert.showAndWait();
        }
        else {
            CustomerDatabase.addcustomer(customerName, customerAddress, customerCity, customerZip, customerPhone);
            Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreenWCalendar.fxml"));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainScreenStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            mainScreenStage.setScene(mainScreenScene);
            mainScreenStage.show();
        }

    }

    public void CancelCustomerAddButton(MouseEvent mouseEvent) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("This will not add the customer and return you to the main screen!");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreenWCalendar.fxml"));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainScreenStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
            mainScreenStage.setScene(mainScreenScene);
            mainScreenStage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){ CustomerCity.setItems(city);
    }

    public void setCountryTextbox(ActionEvent actionEvent) {
        String CurrentCity = CustomerCity.getSelectionModel().getSelectedItem();
        if(CurrentCity.equals("Tokyo")||CurrentCity.equals("Ebisu")||CurrentCity.equals("Ginza")||CurrentCity.equals("Osaka")) {
            CustomerCountryTxtbox.setText("Japan");
        } else {
            CustomerCountryTxtbox.setText("United States");
        }
    }

}

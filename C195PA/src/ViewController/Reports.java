package ViewController;

import Main.database;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Optional;

public class Reports {
    public TextArea reportViewArea;

    public void exitReports(MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("You are about to return to the main screen!");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Parent mainScreenParent = FXMLLoader.load(getClass().getResource("MainScreenWCalendar.fxml"));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainScreenStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
            mainScreenStage.setScene(mainScreenScene);
            mainScreenStage.show();
        }
    }

    public void report1Btn(MouseEvent mouseEvent) {
        try {
            Statement statement = database.getConn().createStatement();
            String query = "SELECT description, monthname(any_value(start)) as 'Month', COUNT(*) as 'Total' FROM appointment GROUP BY description, MONTH(start)";
            ResultSet results = statement.executeQuery(query);
            StringBuilder report1 = new StringBuilder();
            report1.append(String.format("%1$-55s %2$-55s %3$s \n", "Month", "Appointment Type", "Total"));
            report1.append(String.join("", Collections.nCopies(110, "-")));
            report1.append("\n");
            while(results.next()) {
                report1.append(String.format("%1$-55s %2$-60s %3$d \n",
                        results.getString("Month"), results.getString("description"), results.getInt("Total")));
            }
            statement.close();
            reportViewArea.setText(report1.toString());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public void report2Btn(MouseEvent mouseEvent) {
        try {
            StringBuilder reportTwoText;
            try (Statement statement = database.getConn().createStatement()) {
                String query = "select a.contact, a.description, a.start, a.end, c.customerName FROM appointment a, customer c WHERE a.customerId = c.customerId order by contact, start";
                ResultSet results = statement.executeQuery(query);
                reportTwoText = new StringBuilder();
                reportTwoText.append(String.format("%1$-25s %2$-25s %3$-25s %4$-25s %5$s \n",
                        "Agent", "Appointment", "Customer", "Start", "End"));
                reportTwoText.append(String.join("", Collections.nCopies(110, "-")));
                reportTwoText.append("\n");
                while(results.next()) {
                    reportTwoText.append(String.format("%1$-25s %2$-25s %3$-25s %4$-25s %5$s \n",
                            results.getString("contact"), results.getString("description"), results.getString("customerName"),
                            results.getString("start"), results.getString("end")));
                }
            }
            reportViewArea.setText(reportTwoText.toString());
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public void report3Btn(MouseEvent mouseEvent) {
        try {
            Statement statement = database.getConn().createStatement();
            String query = "SELECT customer.customerName, COUNT(*) as 'Total' FROM customer JOIN appointment " +
                    "ON customer.customerId = appointment.customerId GROUP BY customerName";
            ResultSet results = statement.executeQuery(query);
            StringBuilder report3 = new StringBuilder();
            report3.append(String.format("%1$-65s %2$-65s \n", "Customer", "Total Appointments"));
            report3.append(String.join("", Collections.nCopies(110, "-")));
            report3.append("\n");
            while(results.next()) {
                report3.append(String.format("%1$s %2$65d \n",
                        results.getString("customerName"), results.getInt("Total")));
            }
            statement.close();
            reportViewArea.setText(report3.toString());
        } catch (SQLException e) {
           System.out.println("SQLException: " + e.getMessage());
        }
    }
}

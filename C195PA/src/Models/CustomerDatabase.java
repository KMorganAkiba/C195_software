package Models;

import Main.database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDateTime;


public class CustomerDatabase {


    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public static Customer getCustomer(int id) {
        try {
            Statement statement = database.getConn().createStatement();
            String query = "SELECT * FROM customer WHERE customerId='" + id + "'";
            ResultSet results = statement.executeQuery(query);
            if(results.next()) {
                Customer customer = new Customer();
                customer.setCustomerName(results.getString("customerName"));
                statement.close();
                return customer;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }

    // Returns all Customers in Database
    public static ObservableList<Customer> getAllCustomers() {
        allCustomers.clear();
        try {
            Statement statement = database.getConn().createStatement();
            String query = "SELECT customer.customerId, customer.customerName, address.addressId, address.address, address.phone, address.postalCode, city.city"
                    + " FROM customer INNER JOIN address ON customer.addressId = address.addressId "
                    + "INNER JOIN city ON address.cityId = city.cityId";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                Customer customer = new Customer(
                        results.getInt("customerId"),
                        results.getString("customerName"),
                        results.getInt("addressId"),
                        results.getString("address"),
                        results.getString("city"),
                        results.getString("postalCode"),
                        results.getString("phone"));
                allCustomers.add(customer);
            }
            statement.close();
            return allCustomers;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    // Saves new Customer to Database
    public static boolean addcustomer(String name, String address, int cityId, String zip, String phone) {
        try {
            Statement statement = database.getConn().createStatement();
            LocalDateTime now = LocalDateTime.now();
            String queryOne = "INSERT INTO address SET address='" + address + "', address2='" + address + "', phone='" + phone + "', postalCode='" + zip + "', cityId='" + cityId + "', createDate='" + now +"',createdBy='" + "test" + "', lastUpdateBy='" +"test'";
            int updateOne = statement.executeUpdate(queryOne,Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int addId = rs.getInt(1);
            if(updateOne == 1) {
                int addressId = addId;
                String queryTwo = "INSERT INTO customer SET customerName='" + name + "', addressId=" + addressId + ", active=" + 1 + ", createDate='" + now +"',createdBy='" + "test" + "', lastUpdateBy='" +"test'" ;
                int updateTwo = statement.executeUpdate(queryTwo);
                if(updateTwo == 1) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    // Update existing Customer in Database
    public static boolean updateCustomer(int id, int addressid, String name, String address, int cityId, String zip, String phone) {
        try {
            Statement statement = database.getConn().createStatement();
            String queryOne = "UPDATE address SET address='" + address + "', cityId=" + cityId + ", postalCode='" + zip + "', phone='" + phone + "' "
                    + "WHERE addressId=" + addressid ;
            int updateOne = statement.executeUpdate(queryOne);
            if(updateOne == 1) {
                String queryTwo = "UPDATE customer SET customerName='" + name + "', addressId=" + addressid + " WHERE customerId=" + id;
                int updateTwo = statement.executeUpdate(queryTwo);
                if(updateTwo == 1) {
                    return true;
                }
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    // Delete Customer from Database
    public static boolean deleteCustomer(int id, int AID) {
        try {
            Statement statement = database.getConn().createStatement();
            String queryOne = "DELETE FROM customer WHERE customerId=" + id;
            int updateOne = statement.executeUpdate(queryOne);
            if(updateOne == 1) {
                String queryTwo = "DELETE FROM address WHERE addressId=" + AID;
                int updateTwo = statement.executeUpdate(queryTwo);
                if(updateTwo == 1) {
                    return true;
                }
            }
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }
}




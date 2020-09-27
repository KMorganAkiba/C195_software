package Models;

public class Customer {
    private int customerID;
    private int addressID;
    private String customerName;
    private String CustomerAddress;
    private String customerCity;
    private String customerZip;
    private String customerPhone;


    public Customer(int customerID, String customerName,int addressId, String getCustomerAddress, String customerCity, String customerZip, String customerPhone) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.addressID = addressId;
        this.CustomerAddress = getCustomerAddress;
        this.customerCity = customerCity;
        this.customerZip = customerZip;
        this.customerPhone = customerPhone;
    }

    public Customer() {
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public void setCustomerAddress(String getCustomerAddress) {
        this.CustomerAddress = getCustomerAddress;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public void setCustomerZip(String customerZip) {
        this.customerZip = customerZip;
    }

    public String getCustomerZip() {
        return customerZip;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }
}
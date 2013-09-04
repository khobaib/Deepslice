package com.deepslice.model;

public class CustomerInfo {
    private int CustomerID;
    private String CustomerPhone;
    private String CustomerPhoneExt;
    private String CustomerName;
    private String CustomerPassword;
    private int SuburbID;
    private String PostalCode;
    private String UNIT;
    private int StreetId;
    private String CrossStreet;
    private String DeliveryInstructions;
    private String MailingAddress;
    
    public CustomerInfo() {
        // TODO Auto-generated constructor stub
    }
    
    public CustomerInfo(int customerID, String customerPhone, String customerPhoneExt, String customerName,
            String customerPassword, int suburbID, String postalCode, String uNIT, int streetId, String crossStreet,
            String deliveryInstructions, String mailingAddress) {
        CustomerID = customerID;
        CustomerPhone = customerPhone;
        CustomerPhoneExt = customerPhoneExt;
        CustomerName = customerName;
        CustomerPassword = customerPassword;
        SuburbID = suburbID;
        PostalCode = postalCode;
        UNIT = uNIT;
        StreetId = streetId;
        CrossStreet = crossStreet;
        DeliveryInstructions = deliveryInstructions;
        MailingAddress = mailingAddress;
    }



    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public String getCustomerPhone() {
        return CustomerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        CustomerPhone = customerPhone;
    }

    public String getCustomerPhoneExt() {
        return CustomerPhoneExt;
    }

    public void setCustomerPhoneExt(String customerPhoneExt) {
        CustomerPhoneExt = customerPhoneExt;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerPassword() {
        return CustomerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        CustomerPassword = customerPassword;
    }

    public int getSuburbID() {
        return SuburbID;
    }

    public void setSuburbID(int suburbID) {
        SuburbID = suburbID;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String uNIT) {
        UNIT = uNIT;
    }

    public int getStreetId() {
        return StreetId;
    }

    public void setStreetId(int streetId) {
        StreetId = streetId;
    }

    public String getCrossStreet() {
        return CrossStreet;
    }

    public void setCrossStreet(String crossStreet) {
        CrossStreet = crossStreet;
    }

    public String getDeliveryInstructions() {
        return DeliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        DeliveryInstructions = deliveryInstructions;
    }

    public String getMailingAddress() {
        return MailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        MailingAddress = mailingAddress;
    }
}

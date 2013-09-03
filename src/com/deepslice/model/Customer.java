package com.deepslice.model;

import java.io.Serializable;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Customer implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int CustomerID;
    private String EmailID;
    private String Pwd;	
    private String CustomerName;
    private String PhoneNo;
    private boolean isSendEmail;

    public Customer() {
    }

    public Customer(int customerID, String emailID, String pwd, String customerName, String phoneNo, boolean isSendEmail) {
        CustomerID = customerID;
        EmailID = emailID;
        Pwd = pwd;
        CustomerName = customerName;
        PhoneNo = phoneNo;
        this.isSendEmail = isSendEmail;
    }


    public static Customer parseCustomerData(JSONObject custObj){
        Customer customer = new Customer();
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();

        if(custObj != null){
            String jsonString = custObj.toString();
            customer = gson.fromJson(jsonString, Customer.class);
        }
        return customer;
    }




    public int getCustomerID() {
        return CustomerID;
    }
    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }
    public String getEmailID() {
        return EmailID;
    }
    public void setEmailID(String emailID) {
        EmailID = emailID;
    }
    public String getPwd() {
        return Pwd;
    }
    public void setPwd(String pwd) {
        Pwd = pwd;
    }
    public String getCustomerName() {
        return CustomerName;
    }
    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }
    public String getPhoneNo() {
        return PhoneNo;
    }
    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public boolean isSendEmail() {
        return isSendEmail;
    }

    public void setSendEmail(boolean isSendEmail) {
        this.isSendEmail = isSendEmail;
    }
}

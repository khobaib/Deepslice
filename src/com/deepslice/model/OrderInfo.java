package com.deepslice.model;

public class OrderInfo {
    private int LocationID;
    private int CustomerID;
    private int ServiceMethod;
    private boolean IsTimedOrder;
    private String TimedOrder_Date;
    private String TimedOrder_Time;
    private String PaymentStatus;
    private double TotalPrice;
    private int NoOfItems;
    
    public OrderInfo() {
        // TODO Auto-generated constructor stub
    }
        

    public OrderInfo(int locationID, int customerID, int serviceMethod, boolean isTimedOrder, String timedOrder_Date,
            String timedOrder_Time, String paymentStatus, double totalPrice, int noOfItems) {
        LocationID = locationID;
        CustomerID = customerID;
        ServiceMethod = serviceMethod;
        IsTimedOrder = isTimedOrder;
        TimedOrder_Date = timedOrder_Date;
        TimedOrder_Time = timedOrder_Time;
        PaymentStatus = paymentStatus;
        TotalPrice = totalPrice;
        NoOfItems = noOfItems;
    }



    public int getLocationID() {
        return LocationID;
    }

    public void setLocationID(int locationID) {
        LocationID = locationID;
    }

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public int getServiceMethod() {
        return ServiceMethod;
    }

    public void setServiceMethod(int serviceMethod) {
        ServiceMethod = serviceMethod;
    }

    public boolean isIsTimedOrder() {
        return IsTimedOrder;
    }

    public void setIsTimedOrder(boolean isTimedOrder) {
        IsTimedOrder = isTimedOrder;
    }

    public String getTimedOrder_Date() {
        return TimedOrder_Date;
    }

    public void setTimedOrder_Date(String timedOrder_Date) {
        TimedOrder_Date = timedOrder_Date;
    }

    public String getTimedOrder_Time() {
        return TimedOrder_Time;
    }

    public void setTimedOrder_Time(String timedOrder_Time) {
        TimedOrder_Time = timedOrder_Time;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public int getNoOfItems() {
        return NoOfItems;
    }

    public void setNoOfItems(int noOfItems) {
        NoOfItems = noOfItems;
    }
}

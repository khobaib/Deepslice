package com.deepslice.utilities;

import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.deepslice.model.AppInfo;
import com.deepslice.model.CouponDetails;
import com.deepslice.model.CreateOwnPizzaData;
import com.deepslice.model.Customer;
import com.deepslice.model.CustomerInfo;
import com.deepslice.model.NewDealsOrderDetails;
import com.deepslice.model.NewProductOrder;
import com.deepslice.model.NewToppingsOrder;
import com.deepslice.model.OrderInfo;
import com.deepslice.model.PaymentInfo;
import com.deepslice.model.ProductSubCategory;

public class DeepsliceApplication extends Application {
//    private CouponData couponData;
    private CouponDetails couponDetails;
    private NewDealsOrderDetails dealOrderDetails;
    private NewProductOrder halfOder;
    private ProductSubCategory halfCrust;   
    private List<NewToppingsOrder> halfToppingsOrder;
    private NewToppingsOrder halfSauce;
    private List<CreateOwnPizzaData> createPizzaDataList;
    
    private static Context context;
    protected SharedPreferences spCustomer, spOrder, appInfo;
    protected SharedPreferences spCustomerInfo, spPaymentInfo, spOrderInfo;
    
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        spCustomer = PreferenceManager.getDefaultSharedPreferences(context);
        spOrder = PreferenceManager.getDefaultSharedPreferences(context);
        appInfo = PreferenceManager.getDefaultSharedPreferences(context);
        spCustomerInfo = PreferenceManager.getDefaultSharedPreferences(context);
        spPaymentInfo = PreferenceManager.getDefaultSharedPreferences(context);
        spOrderInfo = PreferenceManager.getDefaultSharedPreferences(context);
    }

    
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getAppContext() {
        return context;        
    }
    
    
    public void setOrderType(int type){
        Editor editor = spOrder.edit();
        editor.putInt(Constants.ORDER_TYPE, type);
        editor.commit();
    }
    
    
    public int getOrderType(){
        int type = spOrder.getInt(Constants.ORDER_TYPE, 0);
        return type;
    }
    
    
    public void setOrderReady(Boolean isReady){
        Editor editor = spOrder.edit();
        editor.putBoolean(Constants.IS_ORDER_READY, isReady);
        editor.commit();
    }
    
    
    public Boolean getIsOrderReady(){
        Boolean isReady = spOrder.getBoolean(Constants.IS_ORDER_READY, false);
        return isReady;
    }
    
    
    public void setPartialSelectionSurcharge(String surcharge){
        Editor editor = spOrder.edit();
        editor.putString(Constants.PARTIAL_SELECTION_SURCHARGE, surcharge);
        editor.commit();
    }
    
    
    public String getPartialSelectionSurcharge(){
        String surcharge = spOrder.getString(Constants.PARTIAL_SELECTION_SURCHARGE, "0.00");
        return surcharge;
    }    
    
    
    public void setRememberMe(Boolean isRememberMe){
        Editor editor = spOrder.edit();
        editor.putBoolean(Constants.IS_REMEMBER_ME, isRememberMe);
        editor.commit();
    }
    
    
    public Boolean getIsRememberMe(){
        Boolean isRememberMe = spOrder.getBoolean(Constants.IS_REMEMBER_ME, false);
        return isRememberMe;
    }
    
    
    public void saveOrderInfo(OrderInfo orderInfo){
        Editor editor = spOrderInfo.edit();
        editor.putInt(Constants.ORDER_LOCATION_ID, orderInfo.getLocationID());
        editor.putInt(Constants.ORDER_CUSTOMER_ID, orderInfo.getCustomerID());
        editor.putInt(Constants.ORDER_SERVICE_METHOD, orderInfo.getServiceMethod());
        editor.putBoolean(Constants.ORDER_IS_TIMED_ORDER, orderInfo.isIsTimedOrder());
        editor.putString(Constants.ORDER_TIMED_ORDER_DATE, orderInfo.getTimedOrder_Date());
        editor.putString(Constants.ORDER_TIMED_ORDER_TIME, orderInfo.getTimedOrder_Time());
        editor.putString(Constants.ORDER_PAYMENT_STATUS, orderInfo.getPaymentStatus());
        editor.putString(Constants.ORDER_TOTAL_PRICE, String.valueOf(orderInfo.getTotalPrice()));
        editor.putInt(Constants.ORDER_NO_OF_ITEMS, orderInfo.getNoOfItems());
        editor.commit(); 
    }
    
    public OrderInfo loadOrderInfo(){
        int locationId = spOrderInfo.getInt(Constants.ORDER_LOCATION_ID, 0);
        int custId = spOrderInfo.getInt(Constants.ORDER_CUSTOMER_ID, 0);
        int sMethod = spOrderInfo.getInt(Constants.ORDER_SERVICE_METHOD, 0);
        boolean isTimedOrder = spOrderInfo.getBoolean(Constants.ORDER_IS_TIMED_ORDER, false);
        String toDate = spOrderInfo.getString(Constants.ORDER_TIMED_ORDER_DATE, "");
        String toTime = spOrderInfo.getString(Constants.ORDER_TIMED_ORDER_TIME, "");
        String pStatus = spOrderInfo.getString(Constants.ORDER_PAYMENT_STATUS, "PENDING");
        double tPrice = Double.parseDouble(spOrderInfo.getString(Constants.ORDER_TOTAL_PRICE, "0.0"));
        int noOfItems = spOrderInfo.getInt(Constants.ORDER_NO_OF_ITEMS, 0);
        
        OrderInfo orderInfo = new OrderInfo(locationId, custId, sMethod, isTimedOrder, toDate, toTime,
                pStatus, tPrice, noOfItems);
        return orderInfo;
    }
    
    
    public void savePaymentInfo(PaymentInfo paymentInfo){
        Editor editor = spCustomerInfo.edit();
        editor.putInt(Constants.PAYMENT_NO, paymentInfo.getPaymentNo());
        editor.putString(Constants.PAYMENT_TYPE, paymentInfo.getPaymentType());
        editor.putString(Constants.PAYMENT_SUB_TYPE, paymentInfo.getPaymentSubType());
        editor.putString(Constants.PAYMENT_AMOUNT, String.valueOf(paymentInfo.getAmount()));
        editor.putString(Constants.PAYMENT_CARD_TYPE, paymentInfo.getCardType());
        editor.putString(Constants.PAYMENT_NAME_ON_CARD, paymentInfo.getNameOnCard());
        editor.putString(Constants.PAYMENT_CARD_NO, paymentInfo.getCardNo());
        editor.putString(Constants.PAYMENT_CARD_SECURITY_CODE, paymentInfo.getCardSecurityCode());
        editor.putInt(Constants.PAYMENT_EXPIRY_MONTH, paymentInfo.getExpiryMonth());
        editor.putInt(Constants.PAYMENT_EXPIRY_YEAR, paymentInfo.getExpiryYear());
        editor.commit(); 
    }
    
    public PaymentInfo loadPaymentInfo(){
        int pNo = spCustomerInfo.getInt(Constants.PAYMENT_NO, 1);                // by-default = 1
        String pType = spCustomerInfo.getString(Constants.PAYMENT_TYPE, null);
        String pSubType = spCustomerInfo.getString(Constants.PAYMENT_SUB_TYPE, null);
        double pAmount = Double.parseDouble(spCustomerInfo.getString(Constants.PAYMENT_AMOUNT, "0.0"));
        String pCardType = spCustomerInfo.getString(Constants.PAYMENT_CARD_TYPE, null);
        String pNameOnCard = spCustomerInfo.getString(Constants.PAYMENT_NAME_ON_CARD, null);
        String pCardNo = spCustomerInfo.getString(Constants.PAYMENT_CARD_NO, null);
        String pCardSecurityCode = spCustomerInfo.getString(Constants.PAYMENT_CARD_SECURITY_CODE, null);
        int pExpiryMonth = spCustomerInfo.getInt(Constants.PAYMENT_EXPIRY_MONTH, 0);
        int pExpiryYear = spCustomerInfo.getInt(Constants.PAYMENT_EXPIRY_YEAR, 0);
        
        PaymentInfo paymentInfo = new PaymentInfo(pNo, pType, pSubType, pAmount, pCardType, pNameOnCard,
                pCardNo, pCardSecurityCode, pExpiryMonth, pExpiryYear);
        return paymentInfo;
    }
    
    
    
    public void saveCustomerInfo(CustomerInfo customerInfo){
        Editor editor = spCustomerInfo.edit();
        editor.putInt(Constants.CUSTOMER_ID, customerInfo.getCustomerID());
        editor.putString(Constants.CUSTOMER_NAME, customerInfo.getCustomerName());
        editor.putString(Constants.CUSTOMER_EMAIL, customerInfo.getMailingAddress());
        editor.putString(Constants.CUSTOMER_PHONE, customerInfo.getCustomerPhone());
        editor.putString(Constants.CUSTOMER_PASSWORD, customerInfo.getCustomerPassword());
        editor.putString(Constants.CUSTOMER_PHONE_EXT, customerInfo.getCustomerPhoneExt());
        editor.putInt(Constants.CUSTOMER_SUBURB_ID, customerInfo.getSuburbID());
        editor.putString(Constants.CUSTOMER_POSTAL_CODE, customerInfo.getPostalCode());
        editor.putString(Constants.CUSTOMER_UNIT, customerInfo.getUNIT());
        editor.putString(Constants.CUSTOMER_STREET_NAME, customerInfo.getStreetName());
        editor.putString(Constants.CUSTOMER_CROSS_STREET, customerInfo.getCrossStreet());
        editor.putString(Constants.CUSTOMER_DELIVERY_INSTRUCTIONS, customerInfo.getDeliveryInstructions());
        editor.commit(); 
    }
    
    public CustomerInfo loadCustomerInfo(){
        int customerId = spCustomerInfo.getInt(Constants.CUSTOMER_ID, 0);
        String customerName = spCustomerInfo.getString(Constants.CUSTOMER_NAME, null);
        String customerEmail = spCustomerInfo.getString(Constants.CUSTOMER_EMAIL, null);
        String customerPhone = spCustomerInfo.getString(Constants.CUSTOMER_PHONE, null);
        String customerPass = spCustomerInfo.getString(Constants.CUSTOMER_PASSWORD, "");
        String phoneExt = spCustomerInfo.getString(Constants.CUSTOMER_PHONE_EXT, "123");           // default
        int suburbId = spCustomerInfo.getInt(Constants.CUSTOMER_SUBURB_ID, 0);
        String postalCode = spCustomerInfo.getString(Constants.CUSTOMER_POSTAL_CODE, "");
        String unit = spCustomerInfo.getString(Constants.CUSTOMER_UNIT, "");
        String streetName = spCustomerInfo.getString(Constants.CUSTOMER_STREET_NAME, "");
        String crossStreet = spCustomerInfo.getString(Constants.CUSTOMER_CROSS_STREET, "");
        String deliveryInstructions = spCustomerInfo.getString(Constants.CUSTOMER_DELIVERY_INSTRUCTIONS, "");
        CustomerInfo customerInfo = new CustomerInfo(customerId, customerPhone, phoneExt, customerName,
                customerPass, suburbId, postalCode, unit, streetName, crossStreet, deliveryInstructions, customerEmail);
        return customerInfo;
    }
    
    
    public void saveAppInfo(AppInfo appInfo){
        Editor editor = spCustomer.edit();
        editor.putString(Constants.APP_INFO_CURRENCY_SIGN, appInfo.getCurrencySign());
        editor.putString(Constants.APP_INFO_DELIVERY_CHARGES, appInfo.getDeliveryCharges());
        editor.putString(Constants.APP_INFO_TAX_PER, appInfo.getTaxPer());
        editor.commit();
    }
    
    
    public AppInfo loadAppInfo(){
        String curSign = spCustomer.getString(Constants.APP_INFO_CURRENCY_SIGN, null);
        String delCharge = spCustomer.getString(Constants.APP_INFO_DELIVERY_CHARGES, "0.00");
        String tPer = spCustomer.getString(Constants.APP_INFO_TAX_PER, null);
        AppInfo appInfo = new AppInfo(curSign, delCharge, tPer);
        return appInfo;
    }
       
    
    public void saveCustomer(Customer customer){
        Editor editor = spCustomer.edit();
        editor.putInt(Constants.CUSTOMER_ID, customer.getCustomerID());
        editor.putString(Constants.CUSTOMER_NAME, customer.getCustomerName());
        editor.putString(Constants.CUSTOMER_EMAIL, customer.getEmailID());
        editor.putString(Constants.CUSTOMER_PHONE, customer.getPhoneNo());
        editor.putString(Constants.CUSTOMER_PASSWORD, customer.getPwd());
        editor.putBoolean(Constants.CUSTOMER_FLAG_SEND_EMAIL, customer.isSendEmail());
        editor.commit(); 
    }
    
    
    public Customer loadCustomer(){
        int customerId = spCustomer.getInt(Constants.CUSTOMER_ID, 0);
        String customerName = spCustomer.getString(Constants.CUSTOMER_NAME, null);
        String customerEmail = spCustomer.getString(Constants.CUSTOMER_EMAIL, null);
        String customerPhone = spCustomer.getString(Constants.CUSTOMER_PHONE, null);
        String customerPass = spCustomer.getString(Constants.CUSTOMER_PASSWORD, null);
        boolean isSendEmail = spCustomer.getBoolean(Constants.CUSTOMER_FLAG_SEND_EMAIL, false);
        Customer customer = new Customer(customerId, customerEmail, customerPass, customerName, customerPhone, isSendEmail);
        return customer;
    }
    

    public List<CreateOwnPizzaData> getCreatePizzaDataList() {
        return createPizzaDataList;
    }

    public void setCreatePizzaDataList(List<CreateOwnPizzaData> createPizzaDataList) {
        this.createPizzaDataList = createPizzaDataList;
    }

//    public Order getHalfOder() {
//        return halfOder;
//    }
    
    public NewProductOrder getHalfOder() {
        return halfOder;
    }
    
    public ProductSubCategory getHalfCrust(){
        return halfCrust;
    }
    
    public List<NewToppingsOrder> getHalfToppings(){
        return this.halfToppingsOrder;
    }
    
    public NewToppingsOrder getHalfSauce(){
        return this.halfSauce;
    }

//    public void setHalfOder(Order halfOder, String crust) {
//        this.halfOder = halfOder;
//        this.halfCrust = crust;
//    }
    
    public void setHalfOder(NewProductOrder halfOder, ProductSubCategory crust,
            List<NewToppingsOrder> halfToppingsList, NewToppingsOrder halfSauce) {
        this.halfOder = halfOder;
        this.halfCrust = crust;
        this.halfToppingsOrder = halfToppingsList;
        this.halfSauce = halfSauce;
    }

    public NewDealsOrderDetails getDealOrderDetails() {
        return dealOrderDetails;
    }

    public void setDealOrderDetails(NewDealsOrderDetails dealOrderDetails) {
        this.dealOrderDetails = dealOrderDetails;
    }


//    public CouponData getCouponData() {
//        return couponData;
//    }
//
//    public void setCouponData(CouponData couponData) {
//        this.couponData = couponData;
//    }

    public CouponDetails getCouponDetails() {
        return couponDetails;
    }

    public void setCouponDetails(CouponDetails couponDetails) {
        this.couponDetails = couponDetails;
    }
 
}

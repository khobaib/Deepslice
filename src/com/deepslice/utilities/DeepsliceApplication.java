package com.deepslice.utilities;

import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.deepslice.model.CouponDetails;
import com.deepslice.model.CreateOwnPizzaData;
import com.deepslice.model.Customer;
import com.deepslice.model.CustomerInfo;
import com.deepslice.model.NewDealsOrderDetails;
import com.deepslice.model.NewProductOrder;
import com.deepslice.model.ProductSubCategory;

public class DeepsliceApplication extends Application {
//    private CouponData couponData;
    private CouponDetails couponDetails;
    private NewDealsOrderDetails dealOrderDetails;
    private NewProductOrder halfOder;
    private ProductSubCategory halfCrust;              
    private List<CreateOwnPizzaData> createPizzaDataList;
    
    private static Context context;
    protected SharedPreferences spCustomer, spOrder;
    protected SharedPreferences spCustomerInfo, spPaymentInfo, spOrderInfo;
    
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        spCustomer = PreferenceManager.getDefaultSharedPreferences(context);
        spOrder = PreferenceManager.getDefaultSharedPreferences(context);
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
    
    
    public void setRememberMe(Boolean isRememberMe){
        Editor editor = spOrder.edit();
        editor.putBoolean(Constants.IS_REMEMBER_ME, isRememberMe);
        editor.commit();
    }
    
    
    public Boolean getIsRememberMe(){
        Boolean isRememberMe = spOrder.getBoolean(Constants.IS_REMEMBER_ME, false);
        return isRememberMe;
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
        editor.putInt(Constants.CUSTOMER_STREET_ID, customerInfo.getStreetId());
        editor.putString(Constants.CUSTOMER_CROSS_STREET, customerInfo.getCrossStreet());
        editor.putString(Constants.CUSTOMER_DELIVERY_INSTRUCTIONS, customerInfo.getDeliveryInstructions());
        editor.commit(); 
    }
    
    public CustomerInfo loadCustomerInfo(){
        int customerId = spCustomerInfo.getInt(Constants.CUSTOMER_ID, 0);
        String customerName = spCustomerInfo.getString(Constants.CUSTOMER_NAME, null);
        String customerEmail = spCustomerInfo.getString(Constants.CUSTOMER_EMAIL, null);
        String customerPhone = spCustomerInfo.getString(Constants.CUSTOMER_PHONE, null);
        String customerPass = spCustomerInfo.getString(Constants.CUSTOMER_PASSWORD, null);
        String phoneExt = spCustomerInfo.getString(Constants.CUSTOMER_PHONE_EXT, null);
        int suburbId = spCustomerInfo.getInt(Constants.CUSTOMER_SUBURB_ID, 0);
        String postalCode = spCustomerInfo.getString(Constants.CUSTOMER_POSTAL_CODE, null);
        String unit = spCustomerInfo.getString(Constants.CUSTOMER_UNIT, null);
        int streetId = spCustomerInfo.getInt(Constants.CUSTOMER_STREET_ID, 0);
        String crossStreet = spCustomerInfo.getString(Constants.CUSTOMER_CROSS_STREET, null);
        String deliveryInstructions = spCustomerInfo.getString(Constants.CUSTOMER_DELIVERY_INSTRUCTIONS, null);
        CustomerInfo customerInfo = new CustomerInfo(customerId, customerPhone, phoneExt, customerName,
                customerPass, suburbId, postalCode, unit, streetId, crossStreet, deliveryInstructions, customerEmail);
        return customerInfo;
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

//    public void setHalfOder(Order halfOder, String crust) {
//        this.halfOder = halfOder;
//        this.halfCrust = crust;
//    }
    
    public void setHalfOder(NewProductOrder halfOder, ProductSubCategory crust) {
        this.halfOder = halfOder;
        this.halfCrust = crust;
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

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
    
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        spCustomer = PreferenceManager.getDefaultSharedPreferences(context);
        spOrder = PreferenceManager.getDefaultSharedPreferences(context);
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

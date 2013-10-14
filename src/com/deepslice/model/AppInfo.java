package com.deepslice.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

public class AppInfo {
    
    private String CurrencySign;
    private String DeliveryCharges;
    private String TaxPer;
    
    public AppInfo() {
        // TODO Auto-generated constructor stub
    }

    public AppInfo(String currencySign, String deliveryCharges, String taxPer) {
        CurrencySign = currencySign;
        DeliveryCharges = deliveryCharges;
        TaxPer = taxPer;
    }
    
    public static AppInfo parseAppInfo(JSONObject infoObj){
        AppInfo appInfo = new AppInfo();
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        
        if(infoObj != null){
            String jsonString = infoObj.toString();
            appInfo = gson.fromJson(jsonString, AppInfo.class);
        }
        
        return appInfo;                        
    }

    public String getCurrencySign() {
        return CurrencySign;
    }

    public void setCurrencySign(String currencySign) {
        CurrencySign = currencySign;
    }

    public String getDeliveryCharges() {
        return DeliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        DeliveryCharges = deliveryCharges;
    }

    public String getTaxPer() {
        return TaxPer;
    }

    public void setTaxPer(String taxPer) {
        TaxPer = taxPer;
    }

}

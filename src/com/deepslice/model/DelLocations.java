package com.deepslice.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class DelLocations {
    private String SuburbName;
    private String SuburbAbbr;
    private String PostCode;
    private String LocName;
    private String LocPostalCode;
    private String LocStreet;
    private String LocAddress;
    private String LocLongitude;
    private String LocLatitude;
    private String OpeningTime;
    private String ClosingTime;
    private String LocationID;
    private String SuburbID;

    public DelLocations() {
        // TODO Auto-generated constructor stub
    }


    public DelLocations(String suburbName, String suburbAbbr, String postCode, String locName, String locPostalCode,
            String locStreet, String locAddress, String locLongitude, String locLatitude, String openingTime,
            String closingTime, String locationID, String suburbID) {
        SuburbName = suburbName;
        SuburbAbbr = suburbAbbr;
        PostCode = postCode;
        LocName = locName;
        LocPostalCode = locPostalCode;
        LocStreet = locStreet;
        LocAddress = locAddress;
        LocLongitude = locLongitude;
        LocLatitude = locLatitude;
        OpeningTime = openingTime;
        ClosingTime = closingTime;
        LocationID = locationID;
        SuburbID = suburbID;
    }

    public static List<DelLocations> parseDeliveryLocations(JSONArray locationArray){
        List<DelLocations> deliveryLocationList = new ArrayList<DelLocations>();
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        //        DelLocations aBean;

        try {
            for(int i=0; i<locationArray.length(); i++){

                JSONObject thisLocation = locationArray.getJSONObject(i);
                if(thisLocation!=null){
                    String jsonString = thisLocation.toString();
                    //                aBean=new DelLocations();
                    DelLocations location=gson.fromJson(jsonString, DelLocations.class);
                    //                System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
                    deliveryLocationList.add(location);
                }


            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return deliveryLocationList;
    }



    public String getLocLatitude() {
        return LocLatitude;
    }
    public void setLocLatitude(String locLatitude) {
        LocLatitude = locLatitude;
    }
    public String getLocLongitude() {
        return LocLongitude;
    }
    public void setLocLongitude(String locLongitude) {
        LocLongitude = locLongitude;
    }

    public String getSuburbName() {
        return SuburbName;
    }
    public void setSuburbName(String suburbName) {
        SuburbName = suburbName;
    }
    public String getSuburbAbbr() {
        return SuburbAbbr;
    }
    public void setSuburbAbbr(String suburbAbbr) {
        SuburbAbbr = suburbAbbr;
    }
    public String getPostCode() {
        return PostCode;
    }
    public void setPostCode(String postCode) {
        PostCode = postCode;
    }
    public String getLocName() {
        return LocName;
    }
    public void setLocName(String locName) {
        LocName = locName;
    }
    public String getLocPostalCode() {
        return LocPostalCode;
    }
    public void setLocPostalCode(String locPostalCode) {
        LocPostalCode = locPostalCode;
    }
    public String getLocStreet() {
        return LocStreet;
    }
    public void setLocStreet(String locStreet) {
        LocStreet = locStreet;
    }
    public String getLocAddress() {
        return LocAddress;
    }
    public void setLocAddress(String locAddress) {
        LocAddress = locAddress;
    }
    public String getLocationID() {
        return LocationID;
    }
    public void setLocationID(String locationID) {
        LocationID = locationID;
    }
    public String getSuburbID() {
        return SuburbID;
    }
    public void setSuburbID(String suburbID) {
        SuburbID = suburbID;
    }
    public String getOpeningTime() {
        return OpeningTime;
    }
    public void setOpeningTime(String openingTime) {
        OpeningTime = openingTime;
    }
    public String getClosingTime() {
        return ClosingTime;
    }
    public void setClosingTime(String closingTime) {
        ClosingTime = closingTime;
    }


}

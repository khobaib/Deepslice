package com.deepslice.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LocationPoints {
	public String LocationID;
    public String LocationName;
    public String Longitude;
    public String Latitude;
    public float distance;
    
    
    
    public static List<LocationPoints> parseLocationPoints(JSONArray locationArray){
        List<LocationPoints> locationPointList = new ArrayList<LocationPoints>();
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        //        DelLocations aBean;

        try {
            for(int i=0; i<locationArray.length(); i++){

                JSONObject thisLocation = locationArray.getJSONObject(i);
                if(thisLocation!=null){
                    String jsonString = thisLocation.toString();
                    //                aBean=new DelLocations();
                    LocationPoints location=gson.fromJson(jsonString, LocationPoints.class);
                    //                System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
                    locationPointList.add(location);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return locationPointList;
    }
    
    
    
    
    
	public String getLocationID() {
		return LocationID;
	}
	public void setLocationID(String locationID) {
		LocationID = locationID;
	}
	public String getLocationName() {
		return LocationName;
	}
	public void setLocationName(String locationName) {
		LocationName = locationName;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
}

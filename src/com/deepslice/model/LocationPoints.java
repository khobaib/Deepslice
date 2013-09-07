package com.deepslice.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LocationPoints {
    private String LocationID;
    private String LocationName;
    private String Longitude;
    private String Latitude;
    private String OpeningTime;
    private String ClosingTime;
    private float distance;
    
    
    public LocationPoints() {
        // TODO Auto-generated constructor stub
    }
    
    public static List<LocationPoints> parseLocationPoints(JSONArray locationArray){
        List<LocationPoints> locationPointList = new ArrayList<LocationPoints>();
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        try {
            for(int i=0; i<locationArray.length(); i++){

                JSONObject thisLocation = locationArray.getJSONObject(i);
                if(thisLocation!=null){
                    String jsonString = thisLocation.toString();
                    LocationPoints location = gson.fromJson(jsonString, LocationPoints.class);
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

    public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
}

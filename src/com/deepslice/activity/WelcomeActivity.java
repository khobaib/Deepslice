package com.deepslice.activity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.bugsense.trace.BugSenseHandler;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.AppInfo;
import com.deepslice.model.DeliveryLocation;
import com.deepslice.model.LocationPoints;
import com.deepslice.model.ServerResponse;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;

public class WelcomeActivity extends Activity {

    JsonParser jsonParser = new JsonParser();

    List<DeliveryLocation> deliveryLocationList;
    List<LocationPoints> locPoints;

    Boolean isDeliveryLocationsExist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(WelcomeActivity.this, "92b170cf");
        
        setContentView(R.layout.welcome);

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(WelcomeActivity.this);
        dbInstance.open();
        isDeliveryLocationsExist = dbInstance.isExistsDeliveryLocations() ;
//        dbInstance.cleanDeal(); 
        dbInstance.close();

        new GetLocationPoints().execute();
        new GetGlobalSettings().execute();

    }
    
    
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        BugSenseHandler.startSession(this);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        BugSenseHandler.closeSession(this);
    }
    
    
    
    public class GetGlobalSettings extends AsyncTask<Void, Void, Boolean>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = Constants.ROOT_URL + "GlobalSettings.aspx";
            ServerResponse response = jsonParser.retrieveGETResponse(url, null, Constants.API_RESPONSE_TYPE_JSON_ARRAY);

            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    AppInfo appInfo = AppInfo.parseAppInfo(data.getJSONObject(0));    
                    
                    DeepsliceApplication appInstance = (DeepsliceApplication) getApplication();
                    appInstance.saveAppInfo(appInfo);

                    return true;
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                Log.d(">>>>>>>", "AppInfo Saved successfully.");
            }
        }
    }


    public class GetDeliveryLocation extends AsyncTask<Void, Void, Boolean>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = Constants.ROOT_URL + "DeliveryLocation.aspx?SubQueryName=";
            long reqSendingTime = System.currentTimeMillis();
            ServerResponse response = jsonParser.retrieveGETResponse(url, null, Constants.API_RESPONSE_TYPE_JSON_ARRAY);
            long responseReceivedTime = System.currentTimeMillis();
            Log.d(">>>><<<", "time for cloud retrieve = " + (responseReceivedTime - reqSendingTime)/1000 + " second");
            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    deliveryLocationList = DeliveryLocation.parseDeliveryLocations(data);
                    long arrayParsedTime = System.currentTimeMillis();
                    Log.d(">>>><<<", "time for array parsing = " + (arrayParsedTime - responseReceivedTime)/1000 + " second");

                    return true;
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                updateDbDeliveryLocation();
//                updateResultsInUi();
            }
        }
    }
    
    
    final Handler mHandler = new Handler();
    final Runnable mUpdateResults = new Runnable() {        
        public void run() {            
            updateResultsInUi();        
        }    
    };


    public void updateDbDeliveryLocation(){
        
        Thread t = new Thread() {            
            public void run() {                
                long dbInsertStartTime = System.currentTimeMillis();
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(WelcomeActivity.this);
                dbInstance.open();
                dbInstance.insertProdDeliveryLocations(deliveryLocationList);
                dbInstance.close();
                long dbInsertEndTime = System.currentTimeMillis();
                Log.d(">>>><<<", "time for db insertion of " + deliveryLocationList.size() + " delivery location = " + (dbInsertEndTime - dbInsertStartTime)/1000 + " second");

                mHandler.post(mUpdateResults);            
            }
        };        
        t.start();  
    }


    public class GetLocationPoints extends AsyncTask<Void, Void, Boolean>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = Constants.ROOT_URL + "GetLocationsPoints.aspx";
            ServerResponse response = jsonParser.retrieveGETResponse(url, null, Constants.API_RESPONSE_TYPE_JSON_ARRAY);

            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    locPoints = LocationPoints.parseLocationPoints(data);                    

                    // Here we store in static data field but we have to create a db table for it
//                    AppProperties.locationPointsList = locPoints;
                    System.out.println("Got location points: "+ locPoints.size());

                    return true;
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(WelcomeActivity.this);
                dbInstance.open();
                dbInstance.insertStoreLocations(locPoints);
                dbInstance.close();
                
                
                if(isDeliveryLocationsExist == false)
                    new GetDeliveryLocation().execute();
                else
                    updateResultsInUi();
            }
        }
    }


    private void updateResultsInUi() { 
        Intent intent = new Intent(WelcomeActivity.this, PickupDeliverActivity.class);
        startActivity(intent);
        WelcomeActivity.this.finish();

    }

}
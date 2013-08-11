package com.deepslice.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.DelLocations;
import com.deepslice.model.LocationPoints;
import com.deepslice.model.ServerResponse;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WelcomeActivity extends Activity {

    JsonParser jsonParser = new JsonParser();

    List<DelLocations> deliveryLocationList;
    List<LocationPoints> locPoints;

    Boolean isDeliveryLocationsExist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(WelcomeActivity.this);
        dbInstance.open();
        isDeliveryLocationsExist = dbInstance.isExistsDeliveryLocations() ;
        dbInstance.cleanDeal(); 
        dbInstance.close();

        new GetLocationPoints().execute();

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
            ServerResponse response = jsonParser.retrieveGETResponse(url, null);
            long responseReceivedTime = System.currentTimeMillis();
            Log.d(">>>><<<", "time for cloud retrieve = " + (responseReceivedTime - reqSendingTime)/1000 + " second");
            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    deliveryLocationList = DelLocations.parseDeliveryLocations(data);
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
        
        // running in a thread because we want to show the progressbar loading in UI when rows are INSERTED in db.
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
            ServerResponse response = jsonParser.retrieveGETResponse(url, null);

            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    locPoints = LocationPoints.parseLocationPoints(data);

                    // Here we store in static data field but we have to create a db table for it
                    AppProperties.locationPointsList=locPoints;
                    System.out.println("Got location points: "+locPoints.size());

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
//            if(pDialog.isShowing())
//                pDialog.dismiss();
            if(result){
                if(isDeliveryLocationsExist == false)
                    new GetDeliveryLocation().execute();
                else
                    updateResultsInUi();
            }
        }
    }


    //    String serverResponseDLocs;
    //    protected void getDeliveryLocations() {     
    //
    //        new GetLocationPoints().execute();
    //
    //    }


    private void updateResultsInUi() { 
        Intent intent = new Intent(WelcomeActivity.this, PickupDeliverActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        WelcomeActivity.this.finish();

    }

}
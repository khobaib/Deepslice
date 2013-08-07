package com.deepslice.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.deepslice.database.AppDao;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.DelLocations;
import com.deepslice.model.LocationPoints;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WelcomeActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        boolean syncStatus =false;

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(WelcomeActivity.this);
        dbInstance.open();
        syncStatus=dbInstance.recordExistsDeliveryLocatoins() ;
        dbInstance.cleanDeal(); 
        dbInstance.close();

        //        AppDao dao=null;
        //        try {
        //            dao=AppDao.getSingleton(getApplicationContext());
        //            dao.openConnection();
        //
        //            syncStatus=dao.recordExistsDeliveryLocatoins() ;
        //            if(syncStatus)
        //            {
        //                //				AppProperties.deliveryLocationsList=dao.getAllDeliveryLocations();
        //            }
        //            //			dao.insertOrUpdateList(questionList);
        //
        //
        //        } catch (Exception ex)
        //        {
        //            System.out.println(ex.getMessage());
        //        }finally{
        //            if(null!=dao)
        //                dao.closeConnection();
        //        }

        getDeliveryLocations(syncStatus);

    }


    final Handler mHandler = new Handler();
    final Runnable mUpdateResults = new Runnable() {        
        public void run() {            
            updateResultsInUi();        
        }    
    };
    String serverResponseDLocs;
    protected void getDeliveryLocations(final boolean synced) {        

        Thread t = new Thread() {            
            public void run() {                

                try {
                    //calling the auth service
                    if(synced==false)
                        populateDeliveryLocations();

                    populateLocationPoints();
                } catch (Exception ex)
                {
                    System.out.println(ex.getMessage());
                }
                mHandler.post(mUpdateResults);            
            }


        };        
        t.start();    	
    }
    public void populateDeliveryLocations() {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(Constants.ROOT_URL+"/DeliveryLocation.aspx?SubQueryName=");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                System.out.println("Failed to download file");
            }


            serverResponseDLocs = builder.toString();

            //////////////////////////////////////////////////////////
            String errorMessage="";
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            JSONArray results = new JSONArray(serverResponseDLocs);
            JSONObject respOuter = results.getJSONObject(0);
            JSONObject resp = respOuter.getJSONObject("Response");
            //	      String status = resp.getString("Status");
            JSONArray resultsArray =null;
            Object data= resp.get("Data");
            boolean dataExists=false;
            if(data instanceof JSONArray)
            {
                resultsArray =(JSONArray)data;
                dataExists=true;
            }

            JSONObject errors = resp.getJSONObject("Errors");

            boolean hasError=errors.has("Message");
            if(hasError)
            {
                errorMessage=errors.getString("Message");
                System.out.println("Error:"+errorMessage);
            }

            ArrayList<DelLocations> deliveryLocationList = new ArrayList<DelLocations>();

            if(dataExists==true)
            {
                DelLocations aBean;
                for(int i=0; i<resultsArray.length(); i++){
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    if(jsResult!=null){
                        String jsonString = jsResult.toString();
                        aBean=new DelLocations();
                        aBean=gson.fromJson(jsonString, DelLocations.class);
                        //                System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
                        deliveryLocationList.add(aBean);
                    }
                }
            }

            DeepsliceDatabase dbInstance = new DeepsliceDatabase(WelcomeActivity.this);
            dbInstance.open();
            dbInstance.insertProdDeliveryLocations(deliveryLocationList);
            dbInstance.close();

            //            AppDao dao=null;
            //            try {
            //                dao=AppDao.getSingleton(getApplicationContext());
            //                dao.openConnection();
            //
            //                dao.insertProdDeliveryLocations(deliveryLocationList);
            //
            //            } catch (Exception ex)
            //            {
            //                System.out.println(ex.getMessage());
            //            }finally{
            //                if(null!=dao)
            //                    dao.closeConnection();
            //            }
            //	      AppProperties.deliveryLocationsList=deliveryLocationList;
            System.out.println("Got delivery locations: "+deliveryLocationList.size());
            //////////////////////////////////////////////////////////
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void populateLocationPoints() {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(Constants.ROOT_URL+"/GetLocationsPoints.aspx");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                System.out.println("Failed to download file");
            }


            serverResponseDLocs = builder.toString();

            //////////////////////////////////////////////////////////
            String errorMessage="";
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            JSONArray results = new JSONArray(serverResponseDLocs);
            JSONObject respOuter = results.getJSONObject(0);
            JSONObject resp = respOuter.getJSONObject("Response");
            //	      String status = resp.getString("Status");
            JSONArray resultsArray =null;
            Object data= resp.get("Data");
            boolean dataExists=false;
            if(data instanceof JSONArray)
            {
                resultsArray =(JSONArray)data;
                dataExists=true;
            }

            JSONObject errors = resp.getJSONObject("Errors");

            boolean hasError=errors.has("Message");
            if(hasError)
            {
                errorMessage=errors.getString("Message");
                System.out.println("Error:"+errorMessage);
            }

            ArrayList<LocationPoints> locPoints = new ArrayList<LocationPoints>();

            if(dataExists==true)
            {
                LocationPoints aBean;
                for(int i=0; i<resultsArray.length(); i++){
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    if(jsResult!=null){
                        String jsonString = jsResult.toString();
                        aBean=new LocationPoints();
                        aBean=gson.fromJson(jsonString, LocationPoints.class);
                        //                System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
                        locPoints.add(aBean);
                    }
                }
            }

            AppProperties.locationPointsList=locPoints;
            System.out.println("Got location points: "+locPoints.size());
            //////////////////////////////////////////////////////////
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void updateResultsInUi() { 
        Intent intent = new Intent(WelcomeActivity.this,
                PickupDeliverActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        WelcomeActivity.this.finish();

    }

}
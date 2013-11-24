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
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bugsense.trace.BugSenseHandler;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.DeliveryLocation;
import com.deepslice.model.LocationDetails;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DeliverySuburbActivity extends Activity {

    ListView listview;
    StateListAdapter eventsAdapter;	
    String postCode="",suburbName="";
    EditText input_text ;
    ProgressDialog pd;
    ImageView clearTextIcon;
    List<DeliveryLocation> deliveryLocationList;
    LocationDetails selectedLocation;
    DeliveryLocation selectedDeliveryLocation;
    ArrayList<DeliveryLocation> locList;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(this, "92b170cf");
        
        setContentView(R.layout.delivery_suburb);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        listview = (ListView) findViewById(R.id.listView1);

        deliveryLocationList = new ArrayList<DeliveryLocation>();
        eventsAdapter = new StateListAdapter(this,R.layout.line_item_del, deliveryLocationList);
        listview.setAdapter(eventsAdapter);

        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                selectedDeliveryLocation = (DeliveryLocation) parent.getItemAtPosition(position);
                if (selectedDeliveryLocation != null) {
                    postCode = selectedDeliveryLocation.getPostCode();
                    suburbName = selectedDeliveryLocation.getSuburbName();
                    getLocationDetails(selectedDeliveryLocation.getLocationID()) ;
                    //					AppProperties.saveLocationObj(DeliverySuburbActivity.this, eBean);
                    //					Intent intent = new Intent(DeliverySuburbActivity.this,DeliveryAddress.class);
                    //					Bundle bundle = new Bundle();
                    //					bundle.putString("location",eBean.getSuburbName()+" "+eBean.getPostCode());
                    //					bundle.putString("store",eBean.getLocName());
                    //					bundle.putString("suburbId",eBean.getSuburbID());
                    //					bundle.putInt("position", position);
                    //					intent.putExtras(bundle);
                    //					startActivityForResult(intent, 56);
                }
            }
        });


        clearTextIcon = (ImageView) findViewById(R.id.imageView3);
        clearTextIcon.setVisibility(View.INVISIBLE);
        clearTextIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //				clearTextIcon.setVisibility(View.INVISIBLE);
                input_text.setText("");
                //				deliveryLocationList=new ArrayList<DelLocations>();
                //				eventsAdapter = new StateListAdapter(DeliverySuburbActivity.this,R.layout.line_item_del, deliveryLocationList);
                //				eventsAdapter.notifyDataSetChanged();
                //				listview.setAdapter(eventsAdapter);
            }
        });


        input_text = (EditText) findViewById(R.id.autoCompleteTextView1);
        input_text.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String st = input_text.getText().toString();
                if (st.trim().length() > 0) {
                    clearTextIcon.setVisibility(View.VISIBLE);
                    getDeliveryLocations(st);
                }
                if (st.trim().length() <= 0) {
                    clearTextIcon.setVisibility(View.INVISIBLE);
                    //						input_text.setText("");
                    deliveryLocationList = new ArrayList<DeliveryLocation>();
                    eventsAdapter = new StateListAdapter(DeliverySuburbActivity.this,R.layout.line_item_del, deliveryLocationList);
                    eventsAdapter.notifyDataSetChanged();
                    listview.setAdapter(eventsAdapter);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        //        ImageView emgBack= (ImageView)findViewById(R.id.farwordImageView);
        //        emgBack.setOnClickListener(new OnClickListener() {
        //            public void onClick(View v) {
        //                finish();
        //            }
        //        });

        //        boolean syncStatus = false;

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DeliverySuburbActivity.this);
        dbInstance.open();
        locList = dbInstance.getAllDeliveryLocations();
        //        syncStatus=dbInstance.isExistsDeliveryLocations() ;
        //        if(syncStatus){
        //            locList=dbInstance.getAllDeliveryLocations();
        //        }
        //        else{
        //            locList=new ArrayList<DeliveryLocation>();
        //        }
        dbInstance.close();
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


    protected void getDeliveryLocations(final String prefixCode) {


        try{
            if(AppProperties.isNull(prefixCode))
                return;


            deliveryLocationList = new ArrayList<DeliveryLocation>();

            for (DeliveryLocation delLocations : locList) {

                if(delLocations.getSuburbName().toLowerCase().startsWith(prefixCode.toLowerCase()) || 
                        delLocations.getSuburbAbbr().toLowerCase().startsWith(prefixCode.toLowerCase()) ||
                        delLocations.getPostCode().toLowerCase().startsWith(prefixCode.toLowerCase()) 
                )
                    deliveryLocationList.add(delLocations);
            }
            eventsAdapter = new StateListAdapter(this,R.layout.line_item_del, deliveryLocationList);
            eventsAdapter.notifyDataSetChanged();
            listview.setAdapter(eventsAdapter);	

            //			System.out.println("====>>>>> "+searchedList.size());
            //			AppProperties.deliveryLocationsSearched=searchedList;
            //			myAdapter = new MyListAdapter(this,R.layout.line_item, searchedList);
            //			listview.setAdapter(myAdapter);
            //			myAdapter.notifyDataSetChanged();

        } catch (Exception e){
            e.printStackTrace();
        }
    }



//    final Handler mHandler = new Handler();
//    final Runnable mUpdateResults = new Runnable() {        
//        public void run() {            
//            updateResultsInUi();        
//        }    
//    };
//    String serverResponse;
//    protected void getDeliveryLocationsOld(final String postCodeOrSubId) {        
//        pd = ProgressDialog.show(DeliverySuburbActivity.this, "", "Please wait...", true, false);
//
//        Thread t = new Thread() {            
//            public void run() {                
//
//                try {
//                    //calling the auth service
//                    startLongRunningOperation(postCodeOrSubId);
//                } catch (Exception ex)
//                {
//                    System.out.println(ex.getMessage());
//                }
//                mHandler.post(mUpdateResults);            
//            }
//
//
//        };        
//        t.start();    	
//    }
//    String delLocError="";
//    public void startLongRunningOperation(final String postCodeOrSubId) {
//
//        //		storeLocations=AppProperties.getCityAllCarersFromCache(cityName);
//        //		if(null != storeLocations && storeLocations.size()>0)
//        //			return;
//
//        StringBuilder builder = new StringBuilder();
//        HttpClient client = new DefaultHttpClient();
//
//        HttpGet httpGet = new HttpGet(Constants.ROOT_URL+"/DeliveryLocation.aspx?SubQueryName="+postCodeOrSubId);
//        try {
//            HttpResponse response = client.execute(httpGet);
//            StatusLine statusLine = response.getStatusLine();
//            int statusCode = statusLine.getStatusCode();
//            if (statusCode == 200) {
//                HttpEntity entity = response.getEntity();
//                InputStream content = entity.getContent();
//                BufferedReader reader = new BufferedReader(
//                        new InputStreamReader(content));
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    builder.append(line);
//                }
//            } else {
//                System.out.println("Failed to download file");
//            }
//
//
//            serverResponse = builder.toString();
//
//            //////////////////////////////////////////////////////////
//            String errorMessage="";
//            if (statusCode==200){
//                GsonBuilder gsonb = new GsonBuilder();
//                Gson gson = gsonb.create();
//                JSONArray results = new JSONArray(serverResponse);
//                JSONObject respOuter = results.getJSONObject(0);
//                JSONObject resp = respOuter.getJSONObject("Response");
//                String status = resp.getString("Status");
//                JSONArray resultsArray =null;
//                Object data= resp.get("Data");
//                boolean dataExists=false;
//                if(data instanceof JSONArray)
//                {
//                    resultsArray =(JSONArray)data;
//                    dataExists=true;
//                }
//
//                JSONObject errors = resp.getJSONObject("Errors");
//
//                boolean hasError=errors.has("Message");
//                if(hasError)
//                {
//                    errorMessage=errors.getString("Message");
//                    System.out.println("Error:"+errorMessage);
//                }
//
//                deliveryLocationList = new ArrayList<DeliveryLocation>();
//
//                if(dataExists==true)
//                {
//                    DeliveryLocation aBean;
//                    for(int i=0; i<resultsArray.length(); i++){
//                        JSONObject jsResult = resultsArray.getJSONObject(i);
//                        if(jsResult!=null){
//                            String jsonString = jsResult.toString();
//                            aBean=new DeliveryLocation();
//                            aBean=gson.fromJson(jsonString, DeliveryLocation.class);
//                            //                System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
//                            deliveryLocationList.add(aBean);
//                        }
//                    }
//                }
//            }else {
//                deliveryLocationList = new ArrayList<DeliveryLocation>();
//            }
//
//            //////////////////////////// LOOOOOOOOOOOOPPPPPPPPPPPPPPP
//            //////////////////////////////////////////////////////////
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }catch (Exception e) {
//            delLocError=e.getMessage();
//            e.printStackTrace();
//        }
//    }

//    private void updateResultsInUi() { 
//        pd.dismiss();
//
//        try{
//            if(deliveryLocationList.size()>0){
//                eventsAdapter = new StateListAdapter(this,R.layout.line_item_del, deliveryLocationList);
//                eventsAdapter.notifyDataSetChanged();
//                listview.setAdapter(eventsAdapter);
//            }else {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeliverySuburbActivity.this);
//                alertDialog.setTitle("info");
//                alertDialog.setMessage("Connection Error");
//                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        DeliverySuburbActivity.this.finish();
//                        return;
//                    } });
//                alertDialog.create().show();
//            }
//        } catch (Exception e){
//            e.getMessage();
//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeliverySuburbActivity.this);
//            alertDialog.setTitle("Login");
//            alertDialog.setMessage("Connection Error");
//            alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    return;
//                } }); 
//            alertDialog.create().show();
//        }
//
//    }

    ////////////////////////////////////////LIST ADAPTER	
    private class StateListAdapter extends ArrayAdapter<DeliveryLocation> {

        private List<DeliveryLocation> items;


        public StateListAdapter(Context context, int viewResourceId, List<DeliveryLocation> items) {
            super(context, viewResourceId, items);
            this.items = items;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.line_item_del,null);
            }
            DeliveryLocation event = items.get(position);
            if (event != null) {

                TextView title = (TextView) convertView.findViewById(R.id.textView1);

                //				title.setText(event.getSuburbName()+ " "+event.getSuburbAbbr()+ " "+event.getPostCode());
                title.setText(event.getSuburbName() +" "+event.getPostCode());

                convertView.setTag(event);
            }
            return convertView;
        }

    }
    ///////////////////////// END LIST ADAPTER	

    // ////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////

    final Handler mHandlerLoc = new Handler();
    final Runnable mUpdateResultsLoc = new Runnable() {
        public void run() {
            updateResultsInUiLoc();
        }
    };

  String serverResponse;
    protected void getLocationDetails(final String locationId) {
        Log.d("locationId", locationId);
        pd = ProgressDialog.show(DeliverySuburbActivity.this, "", "Please wait...", true, false);
        Thread t = new Thread() {
            public void run() {

                try {
                    // calling the auth service

                    populateLocationDetails(locationId);

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                mHandlerLoc.post(mUpdateResultsLoc);
            }

        };
        t.start();
    }

    public void populateLocationDetails(final String locationId) {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet;
        HttpResponse response;
        StatusLine statusLine;
        int statusCode;
        LocationDetails locsTemp;

        try {
            httpGet = new HttpGet(Constants.ROOT_URL+ "/GetLocationDetail.aspx?LocationID="+ locationId);

            response = client.execute(httpGet);
            statusLine=response.getStatusLine();
            statusCode=statusLine.getStatusCode();
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();

            serverResponse = Utils.convertStreamToString(content);

            String errorMessage = "";
            if (statusCode==200) {
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                JSONArray results = new JSONArray(serverResponse);
                JSONObject respOuter = results.getJSONObject(0);
                JSONObject resp = respOuter.getJSONObject("Response");
                JSONObject errors = resp.getJSONObject("Errors");
                boolean hasError = errors.has("Message");
                if (hasError==false) {
                    JSONArray resultsArray = null;
                    Object data = resp.get("Data");
                    boolean dataExists = false;
                    if (data instanceof JSONArray) {
                        resultsArray = (JSONArray) data;
                        dataExists = true;
                    }

                    //				LocationDetails locs = new LocationDetails();

                    if (dataExists == true) {

                        JSONObject jsResult = resultsArray.getJSONObject(0);
                        if (jsResult != null) {
                            String jsonString = jsResult.toString();
                            selectedLocation = gson.fromJson(jsonString,LocationDetails.class);
                        }
                    }

                }
            }else {
                //selectedLocation=new LocationDetails();
            }


            // ////////////////////////// LOOOOOOOOOOOOPPPPPPPPPPPPPPP
            // ////////////////////////////////////////////////////////
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    private void updateResultsInUiLoc() {
        pd.dismiss();
        if (selectedLocation!=null){
            selectedLocation.setLocPostalCode(postCode);
            selectedLocation.setLocSuburb(suburbName);
            selectedLocation.setSuburbId(selectedDeliveryLocation.getSuburbID());
            selectedLocation.setPostalCode(selectedDeliveryLocation.getPostCode());
            
            AppProperties.saveLocationObj(DeliverySuburbActivity.this, selectedLocation);
            Log.d(".......selected logation............",selectedLocation.getLocPostalCode());

            Intent intent = new Intent(DeliverySuburbActivity.this, DeliveryAddress.class);
            Bundle bundle = new Bundle();
            bundle.putString("location", selectedDeliveryLocation.getSuburbName() + " " + selectedDeliveryLocation.getPostCode());          // display
            bundle.putString("store", selectedDeliveryLocation.getLocName());           // store name - to display in the DateTimeActivity
            bundle.putString("suburbId", selectedDeliveryLocation.getSuburbID());       // need to retrieve street list
            intent.putExtras(bundle);
            startActivity(intent);
        }else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeliverySuburbActivity.this);
            alertDialog.setTitle("info");
            alertDialog.setMessage("Connection Error");
            alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    DeliverySuburbActivity.this.finish();
                    return;
                } });
            alertDialog.create().show();
        }



    }


}
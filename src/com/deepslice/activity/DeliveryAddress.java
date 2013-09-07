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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.Customer;
import com.deepslice.model.CustomerInfo;
import com.deepslice.model.LocationDetails;
import com.deepslice.model.OrderInfo;
import com.deepslice.model.Street;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressLint("ParserError")
public class DeliveryAddress extends Activity implements OnClickListener {

    AutoCompleteTextView streetName;
    Button continueButton;
    private static String[] streets = {};
    private ArrayAdapter<String> streetsAdapter;
    String subUrbId;	

    String location;
    //	String store;
    LocationDetails selectedLocation;
    EditText streetNum;

    List<Street> streetsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_address);
        Bundle b = this.getIntent().getExtras();

        subUrbId = b.getString("suburbId");

        findViewById(R.id.llmenu).setVisibility(View.GONE);
        
        continueButton = (Button) findViewById(R.id.continueButton);
        continueButton.setOnClickListener(this);

        //selectedLocation.setLocPostalCode(b.getString("postCode"));
        TextView loc = (TextView) findViewById(R.id.textView1);
        location = b.getString("location");
        //		store=b.getString("store");
        loc.setText("Delivering to " + location);

        selectedLocation = AppProperties.getLocationObj(DeliveryAddress.this);

        streetName = (AutoCompleteTextView)findViewById(R.id.streetNameEditText);
        streetsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_spinner_text, streets);
        streetName.setAdapter(streetsAdapter);
        getDeliveryLocations();



        streetNum = (EditText)findViewById(R.id.streetNoEditText);
        streetNum.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean focused) {
                findViewById(R.id.llmenu).setVisibility(focused ? View.VISIBLE : View.GONE);

            }
        });
    }

    public void onNumbers(View v) {
        streetNum.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public void onLetters(View v) {
        streetNum.setInputType(InputType.TYPE_CLASS_TEXT);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continueButton:

                EditText unit=(EditText)findViewById(R.id.unitEditText);

                EditText crossStreet=(EditText)findViewById(R.id.crossStreetNameEditText);
                EditText dInstr=(EditText)findViewById(R.id.deliveryInstructionsEditText);



                if (streetNum.getText().toString().trim().length() <= 0) {
                    Toast.makeText(this, "Enter Street Number",Toast.LENGTH_LONG).show();
                    return;
                }
                if (streetName.getText().toString().trim().length() <= 0) {
                    Toast.makeText(this, "Enter Street Name",Toast.LENGTH_LONG).show();
                    return;
                }


                selectedLocation.setUnit(unit.getText().toString());
                selectedLocation.setStreetNum(streetNum.getText().toString());
                selectedLocation.setStreetName(streetName.getText().toString());
                selectedLocation.setCrossStreetName(crossStreet.getText().toString());
                selectedLocation.setDeliveryInstructions(dInstr.getText().toString());

                AppProperties.saveLocationObj(DeliveryAddress.this, selectedLocation);
                
//                DeepsliceApplication appInstance = (DeepsliceApplication) getApplication();
//                CustomerInfo custInfo = appInstance.loadCustomerInfo();
//                Customer customer = appInstance.loadCustomer();
//                
//                custInfo.setCustomerID(customer.getCustomerID());
//                custInfo.setCustomerPhone(customer.getPhoneNo());
//                custInfo.setCustomerName(customer.getCustomerName());
//                custInfo.setCustomerPassword(customer.getPwd());
//                custInfo.setSuburbID(Integer.parseInt(selectedLocation.getSuburbId()));
//                custInfo.setPostalCode(selectedLocation.getPostalCode());
//                custInfo.setUNIT(selectedLocation.getUnit());
//                custInfo.setStreetName(selectedLocation.getStreetName());
//                custInfo.setCrossStreet(selectedLocation.getCrossStreetName());
//                custInfo.setDeliveryInstructions(selectedLocation.getDeliveryInstructions());
//                custInfo.setMailingAddress(customer.getEmailID());
//                
//                appInstance.saveCustomerInfo(custInfo);
//                
//                OrderInfo orderInfo = appInstance.loadOrderInfo();
//                orderInfo.setLocationID(Integer.parseInt(selectedLocation.getLocationID()));
//                orderInfo.setCustomerID(customer.getCustomerID());
//                orderInfo.setServiceMethod(Constants.ORDER_TYPE_DELIVERY);
//                appInstance.saveOrderInfo(orderInfo);

                DeepsliceDatabase dbInstance = new DeepsliceDatabase(DeliveryAddress.this);
                dbInstance.open();
                dbInstance.insertLocationHistory(selectedLocation, "True");
                dbInstance.close();


                Intent intent=new Intent(this, DateTimeActivity.class);
//                String location = getIntent().getStringExtra("location");
                String store = getIntent().getStringExtra("store");
//                String suburbId = getIntent().getStringExtra("suburbId");			


                Bundle bundle = new Bundle();
//                bundle.putString("location", location);
                bundle.putString("store", store);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            default:
                break;
        }

    }

    final Handler mHandler = new Handler();
    final Runnable mUpdateResults = new Runnable() {        
        public void run() {            
            updateResultsInUi();        
        }    
    };
    ProgressDialog pd;
    String serverResponseDLocs;
    protected void getDeliveryLocations() {        
        pd = ProgressDialog.show(DeliveryAddress.this, "", "Please wait...", true, false);

        Thread t = new Thread() {            
            public void run() {                

                try {
                    //calling the auth service
                    startLongRunningOperation();
                } catch (Exception ex)
                {
                    System.out.println(ex.getMessage());
                }
                mHandler.post(mUpdateResults);            
            }


        };        
        t.start();    	
    }
    String delLocError="";
    public void startLongRunningOperation() {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(Constants.ROOT_URL+"/GetStreets.aspx?SuburbID=" + subUrbId);
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
            String status = resp.getString("Status");
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

            streetsList = new ArrayList<Street>();

            if(dataExists==true){
                Street street;
                for(int i=0; i<resultsArray.length(); i++){
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    if(jsResult!=null){
                        String jsonString = jsResult.toString();
                        street=new Street();
                        street=gson.fromJson(jsonString, Street.class);
                        streetsList.add(street);
                    }
                }
            }

            streets = new String[streetsList.size()];
            int i=0;
            for (Street streetsBean : streetsList) {
                //				streets[i++]=streetsBean.getStreetName() +" "+streetsBean.getStreetID();
                streets[i++] = streetsBean.getStreetName();

            }

            //////////////////////////////////////////////////////////
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            delLocError=e.getMessage();
            e.printStackTrace();
        }
    }

    private void updateResultsInUi() { 
        if(pd.isShowing())
            pd.dismiss();

        try{
            streetsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_spinner_text, streets);
            streetName.setAdapter(streetsAdapter);		
            streetsAdapter.notifyDataSetChanged();

        } catch (Exception e){
            e.getMessage();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeliveryAddress.this);
            alertDialog.setTitle("Delivery Address");
            alertDialog.setMessage("Connection Error");
            alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                } }); 
            alertDialog.create().show();
        }

    }

}

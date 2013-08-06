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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepslice.database.AppDao;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.CouponDetails;
import com.deepslice.model.Coupons;
import com.deepslice.model.LocationDetails;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.utilities.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CouponsActivity extends Activity {
    SharedPreferences settings;
    EditText input_text ;
    ProgressDialog pd;
    ImageView searchIcon;
    Coupons couponsVo;
    String locationId="0";
    TextView resultShow;
    Button applyNow;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        resultShow = (TextView) findViewById(R.id.textView1);
        resultShow.setText("");

        applyNow	= (Button) findViewById(R.id.button1);
        applyNow.setVisibility(View.INVISIBLE);

        LocationDetails locationObj = AppProperties.getLocationObj(CouponsActivity.this);
        locationId=locationObj.getLocationID();

        searchIcon = (ImageView) findViewById(R.id.imageView3);
        searchIcon.setVisibility(View.INVISIBLE);
        searchIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //				clearTextIcon.setVisibility(View.INVISIBLE);
                getCouponInfo();
            }
        });
        input_text = (EditText) findViewById(R.id.autoCompleteTextView1);
        input_text.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String st=input_text.getText().toString();
                if (st.trim().length() > 0) 
                {
                    searchIcon.setVisibility(View.VISIBLE);
                }
                if (st.trim().length() <= 0) 
                {
                    searchIcon.setVisibility(View.INVISIBLE);
                    //						input_text.setText("");
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {


            }
        });

        applyNow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(couponsVo==null)
                    return;

                String couponType="N";

                if("True".equalsIgnoreCase(couponsVo.getIsFixed()))
                    couponType=AppProperties.COUPON_TYPE_FIXED;
                else if("True".equalsIgnoreCase(couponsVo.getIsPercentage()))
                    couponType=AppProperties.COUPON_TYPE_PERCENTAGE;
                else if("True".equalsIgnoreCase(couponsVo.getIsDiscountedProduct()))
                    couponType=AppProperties.COUPON_TYPE_PRODUCTS;

                AppSharedPreference.putData(CouponsActivity.this, "couponId", couponsVo.getCouponID());
                AppSharedPreference.putData(CouponsActivity.this, "couponCode", couponsVo.getCouponCode());
                AppSharedPreference.putData(CouponsActivity.this, "couponType", couponType);
                AppSharedPreference.putData(CouponsActivity.this, "couponAmount", couponsVo.getAmount());

                if(AppProperties.COUPON_TYPE_PRODUCTS.equals(couponType))
                {
                    getCouponDetails(couponsVo.getCouponID());
                }
                else
                {
                    appliedSuccess();
                }

            }


        });		
    }

    private void appliedSuccess() {
        AlertDialog alertDialog = new AlertDialog.Builder(CouponsActivity.this).create();
        alertDialog.setTitle("Add Coupon");
        alertDialog.setMessage("Coupon successfully applied!");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i=new Intent(CouponsActivity.this,PaymentSelectionActivity.class);
                startActivity(i);
                finish();
                return;
            } }); 
        alertDialog.setCancelable(false);
        alertDialog.show();


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////getting coupon info ///////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    final Handler mHandler = new Handler();
    final Runnable mUpdateResults = new Runnable() {        
        public void run() {            
            updateResultsInUi();        
        }    
    };
    String serverResponse;
    protected void getCouponInfo() {        
        pd = ProgressDialog.show(CouponsActivity.this, "", "Please wait...", true, false);

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

        HttpGet httpGet = new HttpGet(Constants.ROOT_URL+"/GetCoupons.aspx?LocationID="+locationId+"&CouponCode="+input_text.getText().toString()+"&Filter=Coupons");
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


            serverResponse = builder.toString();


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
        pd.dismiss();

        try{

            // ////////////////////////////////////////////////////////
            String errorMessage = "";
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            JSONArray results = new JSONArray(serverResponse);
            JSONObject respOuter = results.getJSONObject(0);
            JSONObject resp = respOuter.getJSONObject("Response");
            String status = resp.getString("Status");


            JSONObject errors = resp.getJSONObject("Errors");
            boolean hasError = errors.has("Message");
            if (hasError) {
                errorMessage = errors.getString("Message");
                System.out.println("Error:" + errorMessage);
                resultShow.setText(errorMessage);
            }

            JSONArray resultsArray =null;
            Object data= resp.get("Data");
            boolean dataExists=false;
            if(data instanceof JSONArray)
            {
                resultsArray =(JSONArray)data;
                dataExists=true;
            }

            if (dataExists == true) {
                JSONObject jsResult = resultsArray.getJSONObject(0);
                String jsonString = jsResult.toString();
                couponsVo = new Coupons();
                couponsVo = gson.fromJson(jsonString, Coupons.class);
                resultShow.setText(couponsVo.getDisplayText());
                applyNow.setVisibility(View.VISIBLE);
            }
            // ////////////////////////// LOOOOOOOOOOOOPPPPPPPPPPPPPPP


        } catch (Exception e){
            e.printStackTrace();
            AlertDialog alertDialog = new AlertDialog.Builder(CouponsActivity.this).create();
            alertDialog.setTitle("Login");
            alertDialog.setMessage(e.getMessage());
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                } }); 
            alertDialog.show();
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////getting coupon details for products type ///////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    final Handler mHandlerDetails = new Handler();
    final Runnable mUpdateResultsDetails = new Runnable() {        
        public void run() {            
            updateResultsInUiDetails();        
        }    
    };
    String serverResponseDetails;
    protected void getCouponDetails(final String couponId) {        
        pd = ProgressDialog.show(CouponsActivity.this, "", "Please wait...", true, false);

        Thread t = new Thread() {            
            public void run() {                

                try {
                    //calling the auth service
                    fetchCouponDetails(couponId);
                } catch (Exception ex)
                {
                    System.out.println(ex.getMessage());
                }
                mHandlerDetails.post(mUpdateResultsDetails);            
            }


        };        
        t.start();    	
    }

    public void fetchCouponDetails(final String couponId) {

        delLocError="";

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(Constants.ROOT_URL+"/GetCouponDetail.aspx?CouponID="+couponId);
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


            serverResponseDetails = builder.toString();

            //////////////////////////////////////////////////////////
            String errorMessage="";
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            JSONArray results = new JSONArray(serverResponseDetails);
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

            ArrayList<CouponDetails> couponDetails = new ArrayList<CouponDetails>();

            if(dataExists==true)
            {
                CouponDetails aBean;
                for(int i=0; i<resultsArray.length(); i++){
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    if(jsResult!=null){
                        String jsonString = jsResult.toString();
                        aBean=new CouponDetails();
                        aBean=gson.fromJson(jsonString, CouponDetails.class);
                        //                System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
                        couponDetails.add(aBean);
                    }
                }
            }
            System.out.println(couponDetails.size());

            DeepsliceDatabase dbInstance = new DeepsliceDatabase(CouponsActivity.this);
            dbInstance.open();
            dbInstance.updateOrderDetails(couponDetails);
            dbInstance.close();

            //	      AppDao dao=null;
            //			try {
            //				dao=AppDao.getSingleton(getApplicationContext());
            //				dao.openConnection();
            //				
            //				dao.updateOrderDetails(couponDetails);
            //				
            //			} catch (Exception ex)
            //			{
            //				System.out.println(ex.getMessage());
            //			}finally{
            //				if(null!=dao)
            //					dao.closeConnection();
            //			}

            //////////////////////////////////////////////////////////
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            delLocError=e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            delLocError=e.getMessage();
        }catch (Exception e) {
            e.printStackTrace();
            delLocError=e.getMessage();
        }	
    }

    private void updateResultsInUiDetails() { 
        pd.dismiss();

        try{

            //start operations	   
            if(AppProperties.isNull(delLocError))
            {
                appliedSuccess();
            }
            else
            {
                AlertDialog alertDialog = new AlertDialog.Builder(CouponsActivity.this).create();
                alertDialog.setTitle("Add Coupon");
                alertDialog.setMessage(delLocError);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        return;
                    } }); 
                alertDialog.show();

            }

        } catch (Exception e){
            e.getMessage();
            AlertDialog alertDialog = new AlertDialog.Builder(CouponsActivity.this).create();
            alertDialog.setTitle("Add Coupon");
            alertDialog.setMessage(e.getMessage());
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                } }); 
            alertDialog.show();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent i=new Intent(CouponsActivity.this,MyOrderActivity.class);
            startActivity(i);
            this.finish();
            return false;     
        }
        return super.onKeyDown(keyCode, event);
    }


}
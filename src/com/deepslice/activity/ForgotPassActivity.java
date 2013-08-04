package com.deepslice.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.deepslice.utilities.Constants;
import com.deepslice.utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ForgotPassActivity extends Activity {
	EditText textEmail ;
	int loginStatus = -1;
	ProgressDialog pd;
	Display display = null;
	final Handler mHandler = new Handler();
	final Runnable mUpdateResults = new Runnable() {        
		public void run() {            
			updateResultsInUi();        
		}    
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setDisplay(getWindowManager().getDefaultDisplay());

        Button txtSignup = (Button) findViewById(R.id.startOrderingButton);
        txtSignup.setOnClickListener(new OnClickListener() {    
        	public void onClick(View v) {
        		Intent intent = new Intent(ForgotPassActivity.this,PickupDeliverActivity.class);
        		startActivity(intent);
        		ForgotPassActivity.this.finish();
        	}
        });

        ImageView farwordImageView = (ImageView) findViewById(R.id.farwordImageView);
        farwordImageView.setOnClickListener(new OnClickListener() {    
        	public void onClick(View v) {
        		Intent intent = new Intent(ForgotPassActivity.this,LoginActivity.class);
        		startActivity(intent);
        		ForgotPassActivity.this.finish();
        	}
        });
        
		textEmail = (EditText) findViewById(R.id.textEmail);
		textEmail.setText("");
		
		final Button button = (Button) findViewById(R.id.registerButton);
        button.setOnClickListener(new OnClickListener() {    
	        	public void onClick(View v) {
	        		if(textEmail.getText().toString().trim().equals(""))
	        		{
	    				AlertDialog alertDialog = new AlertDialog.Builder(ForgotPassActivity.this).create();
	    			    alertDialog.setTitle("Validation Failed");
	    			    alertDialog.setMessage("Please enter your email address!");
	    			    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	    			      public void onClick(DialogInterface dialog, int which) {
	    			        return;
	    			    } }); 
	    			    alertDialog.show();
	    			    
	    			    return;
	        		}
	        		startLongRunningOperation();
	        	}
	        });        
        
    }
//creating subscriber thread
	
	String serverResponse;
	protected void startLongRunningOperation() {        
		pd = ProgressDialog.show(ForgotPassActivity.this, "", "Authenticating...", true, false);

		Thread t = new Thread() {            
			public void run() {                
			
				try {
					//calling the auth service
					getPassword(textEmail.getText().toString());
				} catch (Exception ex)
				{
					System.out.println(ex.getMessage());
				}
				mHandler.post(mUpdateResults);            
			}

			
		};        
		t.start();    	
	}
	//subscriber thread call back
	private void updateResultsInUi() { 
		pd.dismiss();

		try {

			String errorMessage="";
//			System.out.println(">>>>>>>>>>"+readFeed);
			GsonBuilder gsonb = new GsonBuilder();
		      Gson gson = gsonb.create();
		      JSONArray results = new JSONArray(serverResponse);
		      JSONObject respOuter = results.getJSONObject(0);
		      JSONObject resp = respOuter.getJSONObject("Response");
		      String status = resp.getString("Status");

		      JSONArray data= resp.getJSONArray("Data");
		      boolean dataExists= resp.has("Status");
		      
		      JSONObject errors = resp.getJSONObject("Errors");
		      boolean hasError=errors.has("Message");
		      if(hasError)
		    	  {
		    		   errorMessage=errors.getString("Message");
		    		  System.out.println("Error:"+errorMessage);
		    		  Utils.openErrorDialog(ForgotPassActivity.this, errorMessage);
		    	  }
		      else{
		    	  if(dataExists==true)
			      {
		    		  JSONObject jd = data.getJSONObject(0);
		              String respStatus=jd.getString("Status");
		              if("FAILED".equalsIgnoreCase(respStatus))
		              {
		            	  Utils.openErrorDialog(ForgotPassActivity.this, jd.getString("FailedReason"));
		              }
		              else
		              {
		            	  Utils.openErrorDialog(ForgotPassActivity.this, "Password sent on your email address");
		              }
			      }
		    	  
		      }
		} catch (Exception e){
			e.getMessage();
			AlertDialog alertDialog = new AlertDialog.Builder(ForgotPassActivity.this).create();
		    alertDialog.setTitle("Login");
		    alertDialog.setMessage("Connection Error");
		    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int which) {
		        return;
		    } }); 
		    alertDialog.show();
		}
		
	}
	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display ds) {
		this.display = ds;
	}

	public void getPassword(String user) {
		
		StringBuilder builder = new StringBuilder();
		
		Integer timeoutMs = new Integer(30 * 1000);
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeoutMs);
		HttpConnectionParams.setSoTimeout(httpParams, timeoutMs);
		HttpClient httpclient = new DefaultHttpClient(httpParams);

		try {
				HttpGet httpReq = new HttpGet(Constants.ROOT_URL+"SendUserPassword.aspx?email="+user);
				

				HttpResponse response = null;
				
				response = httpclient.execute(httpReq);
				 
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					
					InputStream content = resEntity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
					
					
				}
				serverResponse = builder.toString();
				
			      


		} catch (Exception e) {
			e.printStackTrace();

		}
	}	 
	
}
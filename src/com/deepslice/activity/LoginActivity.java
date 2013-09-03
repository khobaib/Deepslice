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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepslice.model.UserData;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.utilities.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LoginActivity extends Activity {
	EditText userLogin ;
	EditText userPassword ;
	CheckBox rememberMeCheckBox;
	int loginStatus = -1;
	UserData userObj;
	boolean rememberMe;
	ProgressDialog pd;
	Display display = null;
	String serverResponse;
//	UserBean uBean;
	final Handler mHandler = new Handler();
	final Runnable mUpdateResults = new Runnable() {        
		public void run() {            
			updateResultsInUi();        
		}    
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setDisplay(getWindowManager().getDefaultDisplay());

        rememberMeCheckBox	= (CheckBox) findViewById(R.id.checkBox);
        
        
        UserData savedUserObj = AppProperties.getUserFromSession(getApplicationContext());
        if(null != savedUserObj && !AppProperties.isNull(savedUserObj.getCustomerID()))
        	{
        	AppProperties.userObj=savedUserObj;
        	AppProperties.isLoogedIn=true;

			LoginActivity.this.finish();
        	}
        
        //set the device type on the basis of screen size
        //initialize the page size on the base of device type
        
        Button txtSignup = (Button) findViewById(R.id.startOrderingButton);
        txtSignup.setOnClickListener(new OnClickListener() {    
        	public void onClick(View v) {
                AppSharedPreference.putBoolean(LoginActivity.this,"MyOrder",false);
        		startActivity(new Intent(LoginActivity.this, CustomerDetailsActivity.class));
        	}
        });

        TextView txtForgot = (TextView) findViewById(R.id.textForgot);
        txtForgot.setOnClickListener(new OnClickListener() {    
        	public void onClick(View v) {
        		Intent intent = new Intent(LoginActivity.this,ForgotPassActivity.class);
        		startActivity(intent);
        		LoginActivity.this.finish();        	}
        });
        

		userLogin = (EditText) findViewById(R.id.textEmail);
		userPassword = (EditText) findViewById(R.id.txtPassword);
		userLogin.setText("");
		userPassword.setText("");
		
		final Button button = (Button) findViewById(R.id.registerButton);
        button.setOnClickListener(new OnClickListener() {    
	        	public void onClick(View v) {
	        		String errors="";
	        		if(userLogin.getText().toString().trim().equals(""))
	        		{
	        			errors+="Enter your email!\n";
	        		}
	        		if(userPassword.getText().toString().trim().equals(""))
	        		{
	        			errors+="Enter password!";
	        		}
	        		
	        		if(!errors.trim().equals(""))
	        		{
	    				AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
	    			    alertDialog.setTitle("Validation Failed");
	    			    alertDialog.setMessage(errors);
	    			    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	    			      public void onClick(DialogInterface dialog, int which) {
	    			        return;
	    			    } }); 
	    			    alertDialog.show();
	    			    
	    			    return;
	        		}
	        		rememberMe=rememberMeCheckBox.isChecked();
	        		startLongRunningOperation();
	        	}
	        });        
        
        ImageView farwordImageView = (ImageView) findViewById(R.id.farwordImageView);
        farwordImageView.setOnClickListener(new OnClickListener() {    
        	public void onClick(View v) {
        		LoginActivity.this.finish();
        	}
        });
        
    }
//creating subscriber thread
	protected void startLongRunningOperation() {        
		pd = ProgressDialog.show(LoginActivity.this, "", "Authenticating...", true, false);

		Thread t = new Thread() {            
			public void run() {                
			
				try {
					//calling the auth service
				 authenticateUser(userLogin.getText().toString(),userPassword.getText().toString());
				} catch (Exception ex)
				{
					System.out.println(ex.getMessage());
				}
				mHandler.post(mUpdateResults);            
			}

			
		};        
		t.start();    	
	}
	private void authenticateUser(String user, String pass) {
		
		postLoginRequest(user,pass);
		
	}
	//subscriber thread call back
	private void updateResultsInUi() { 
		pd.dismiss();
		String errorMessage="";
		try{
		GsonBuilder gsonb = new GsonBuilder();
	      Gson gson = gsonb.create();
	      JSONArray results = new JSONArray(serverResponse);
	      JSONObject respOuter = results.getJSONObject(0);
	      JSONObject resp = respOuter.getJSONObject("Response");
	      String status = resp.getString("Status");

	      JSONObject data= resp.getJSONObject("Data");
	      boolean dataExists= data.has("CustomerID");
	      
	      JSONObject errors = resp.getJSONObject("Errors");
	      boolean hasError=errors.has("Message");
	      if(hasError)
	    	  {
	    		   errorMessage=errors.getString("Message");
	    		  System.out.println("Error:"+errorMessage);
	    	  }
	      

	      if(dataExists==true)
	      {
              String jsonString = data.toString();
              userObj=new UserData();
              userObj=gson.fromJson(jsonString, UserData.class);
	      }
	      

			if (hasError) {

				AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
			    alertDialog.setTitle("Error!");
			    alertDialog.setMessage(errorMessage);
			    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {
			        return;
			    } }); 
			    alertDialog.show();
		} else {

			AppProperties.userObj=this.userObj;
			AppProperties.isLoogedIn=true;			
			if(rememberMe)
				{
				AppProperties.saveUserSession(getApplicationContext(),this.userObj);
				}
			
			AppSharedPreference.putData(LoginActivity.this, "customerName", userObj.getCustomerName());
			AppSharedPreference.putData(LoginActivity.this, "customerEmail", userObj.getEmailID());
			AppSharedPreference.putData(LoginActivity.this, "customerPhone", userObj.getPhoneNo());
            if (AppSharedPreference.getBoolean(LoginActivity.this,"MyOrder")){
                AppSharedPreference.putBoolean(LoginActivity.this, "MyOrder",false);
                String orderType=AppSharedPreference.getData(LoginActivity.this, "orderType", null);
                if("Delivery".equalsIgnoreCase(orderType)) {
                    startActivity(new Intent(new Intent(this,LocationFromHistoryActivity.class)));
                    finish();
                }else {
                    startActivity(new Intent(new Intent(this,StoreFromHistoryActivity.class)));
                    finish();
                }
            } else {
    		LoginActivity.this.finish();
            }
		}
			
		} catch (Exception e){
			e.getMessage();
			AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
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

	public void postLoginRequest(String user, String pass) {
		
		StringBuilder builder = new StringBuilder();
		
		Integer timeoutMs = new Integer(30 * 1000);
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeoutMs);
		HttpConnectionParams.setSoTimeout(httpParams, timeoutMs);
		HttpClient httpclient = new DefaultHttpClient(httpParams);

		try {
				HttpGet req = new HttpGet(Constants.ROOT_URL+"Login.aspx?Mailing_Address="+user+"&Password="+pass);

				HttpResponse response = null;
				
				response = httpclient.execute(req);
				 
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
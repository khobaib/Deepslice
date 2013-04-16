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

import com.deepslice.database.AppDao;
import com.deepslice.utilities.AppProperties;
import com.deepslice.vo.LocationDetails;
import com.deepslice.vo.StreetsBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressLint("ParserError")
public class DeliveryAddress extends Activity implements OnClickListener {
	AutoCompleteTextView streetName;
	Button continueButton;
	private static String[] streets={};
	private ArrayAdapter<String> streetsAdapter;
	String subUrbId;	
	ArrayList<StreetsBean> streetsList;
	String location,store;
	LocationDetails selectedLocation;
	EditText streetNum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delivery_address);
		Bundle b = this.getIntent().getExtras();
		
		subUrbId=b.getString("suburbId");
		continueButton = (Button) findViewById(R.id.continueButton);
		continueButton.setOnClickListener(this);
		
		selectedLocation = AppProperties.getLocationObj(DeliveryAddress.this);
		
		TextView loc = (TextView) findViewById(R.id.textView1);
		location=b.getString("location");
		store=b.getString("store");
		loc.setText("Delivering to "+location);
		
		streetName=(AutoCompleteTextView)findViewById(R.id.streetNameEditText);
		streetsAdapter=new ArrayAdapter<String>(getApplicationContext(), R.layout.my_spinner_text,streets);
		streetName.setAdapter(streetsAdapter);
		getDeliveryLocations();
		
		
		
		streetNum=(EditText)findViewById(R.id.streetNoEditText);
		streetNum.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean focused) {
                findViewById(R.id.llmenu).setVisibility(focused?View.VISIBLE:View.GONE);
               
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
			


			if (streetNum.getText().toString().trim().length() <= 0) 
			{
				Toast.makeText(this, "Enter Street Number",Toast.LENGTH_LONG).show();
				return;
			}
			if (streetName.getText().toString().trim().length() <= 0) 
			{
				Toast.makeText(this, "Enter Street Name",Toast.LENGTH_LONG).show();
				return;
			}

			
			selectedLocation.setUnit(unit.getText().toString());
			selectedLocation.setStreetNum(streetNum.getText().toString());
			selectedLocation.setStreetName(streetName.getText().toString());
			selectedLocation.setCrossStreetName(crossStreet.getText().toString());
			selectedLocation.setDeliveryInstructions(dInstr.getText().toString());
			
			AppProperties.saveLocationObj(DeliveryAddress.this, selectedLocation);
			
			//////////
			AppDao dao=null;
			try {
				dao=AppDao.getSingleton(getApplicationContext());
				dao.openConnection();
			
				dao.insertLocationHistory(selectedLocation, "True");
				

			} catch (Exception ex)
			{
				System.out.println(ex.getMessage());
			}finally{
				if(null!=dao)
					dao.closeConnection();
			}

			/////////////////////////
			
			Intent intent=new Intent(this, DateTimeActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("location",location);
			bundle.putString("store",store);
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
		
		HttpGet httpGet = new HttpGet(AppProperties.WEB_SERVICE_PATH+"/GetStreets.aspx?SuburbID="+subUrbId);
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
	      
	      streetsList = new ArrayList<StreetsBean>();

	      if(dataExists==true)
	      {
	    	  StreetsBean aBean;
		      for(int i=0; i<resultsArray.length(); i++){
		    	  JSONObject jsResult = resultsArray.getJSONObject(i);
		          if(jsResult!=null){
		                String jsonString = jsResult.toString();
		                aBean=new StreetsBean();
		                aBean=gson.fromJson(jsonString, StreetsBean.class);
		//                System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
		                streetsList.add(aBean);
		          }
		     }
	      }

	      streets=new String[streetsList.size()];
	      int i=0;
			for (StreetsBean streetsBean : streetsList) {
//				streets[i++]=streetsBean.getStreetName() +" "+streetsBean.getStreetID();
				streets[i++]=streetsBean.getStreetName();
						
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
		pd.dismiss();
		
		try{
		
	      
//			streetName=(AutoCompleteTextView)findViewById(R.id.streetNameEditText);
			//System.out.println("Sizeeeeeeeee>>>>"+streets.length);
			streetsAdapter=new ArrayAdapter<String>(getApplicationContext(), R.layout.my_spinner_text,streets);
			streetName.setAdapter(streetsAdapter);		
			streetsAdapter.notifyDataSetChanged();
			
		} catch (Exception e){
			e.getMessage();
			AlertDialog alertDialog = new AlertDialog.Builder(DeliveryAddress.this).create();
		    alertDialog.setTitle("Delivery Address");
		    alertDialog.setMessage("Connection Error");
		    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int which) {
		        return;
		    } }); 
		    alertDialog.show();
		}
		
	}
	
}

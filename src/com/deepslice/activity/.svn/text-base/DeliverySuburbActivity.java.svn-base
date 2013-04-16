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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.deepslice.database.AppDao;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Utils;
import com.deepslice.vo.DelLocations;
import com.deepslice.vo.LocationDetails;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DeliverySuburbActivity extends Activity {
	SharedPreferences settings;
	ImageView addPic;
	ListView listview;
	StateListAdapter eventsAdapter;	
//	private static ArrayList<DelLocations> storeLocations;
	static String cityName="";
	static String stName="";
	EditText input_text ;
	ProgressDialog pd;
	ImageView clearTextIcon;
	 ArrayList<DelLocations> deliveryLocationList;
	 LocationDetails selectedLocation;
	 DelLocations eBean;
	 ArrayList<DelLocations> locList = null;
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delivery_suburb);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		listview = (ListView) findViewById(R.id.listView1);
		
		deliveryLocationList=new ArrayList<DelLocations>();
		eventsAdapter = new StateListAdapter(this,R.layout.line_item_del, deliveryLocationList);
		listview.setAdapter(eventsAdapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				eBean = (DelLocations) v.getTag();
				if (eBean != null) {
					getLocationDetails(eBean.getLocationID()) ;
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
				String st=input_text.getText().toString();
//				if (st.trim().length() > 0) 
//				{
//					searchIcon.setVisibility(View.VISIBLE);
//				}
				if (st.trim().length() > 0) 
				{
					clearTextIcon.setVisibility(View.VISIBLE);
					getDeliveryLocations(st);
				}
				if (st.trim().length() <= 0) 
				{
						clearTextIcon.setVisibility(View.INVISIBLE);
//						input_text.setText("");
						deliveryLocationList=new ArrayList<DelLocations>();
						eventsAdapter = new StateListAdapter(DeliverySuburbActivity.this,R.layout.line_item_del, deliveryLocationList);
						eventsAdapter.notifyDataSetChanged();
						listview.setAdapter(eventsAdapter);
				}
			
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				
			}
		});
		
		
		ImageView emgBack= (ImageView)findViewById(R.id.farwordImageView);
		emgBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		boolean syncStatus =false;
		AppDao dao=null;
		try {
			dao=AppDao.getSingleton(getApplicationContext());
			dao.openConnection();
			
			syncStatus=dao.recordExistsDeliveryLocatoins() ;
			if(syncStatus)
			{
				locList=dao.getAllDeliveryLocations();
			}
			else{
				locList=new ArrayList<DelLocations>();
			}
			
//			dao.insertOrUpdateList(questionList);
			
		} catch (Exception ex)
		{
			locList=new ArrayList<DelLocations>();
			System.out.println(ex.getMessage());
		}finally{
			if(null!=dao)
				dao.closeConnection();
		}

	}
	
	
	protected void getDeliveryLocations(final String postCodeOrSubId) {


		try{
		if(AppProperties.isNull(postCodeOrSubId))
			return;
			

			deliveryLocationList = new ArrayList<DelLocations>();
			
			for (DelLocations delLocations : locList) {
				
//				boolean inName = Pattern.compile(Pattern.quote(postCodeOrSubId), Pattern.CASE_INSENSITIVE).matcher(delLocations.getSuburbName()).find();
//				boolean inAbbr = Pattern.compile(Pattern.quote(postCodeOrSubId), Pattern.CASE_INSENSITIVE).matcher(delLocations.getSuburbAbbr()).find();
//				boolean inPostcode = Pattern.compile(Pattern.quote(postCodeOrSubId), Pattern.CASE_INSENSITIVE).matcher(delLocations.getPostCode()).find();
				
//				if(delLocations.getSuburbName().toLowerCase().contains(postCodeOrSubId.toLowerCase()) || 
//						delLocations.getSuburbAbbr().toLowerCase().contains(postCodeOrSubId.toLowerCase()) ||
//						delLocations.getPostCode().toLowerCase().contains(postCodeOrSubId.toLowerCase()) 
//						)
				if(delLocations.getSuburbName().toLowerCase().startsWith(postCodeOrSubId.toLowerCase()) || 
						delLocations.getSuburbAbbr().toLowerCase().startsWith(postCodeOrSubId.toLowerCase()) ||
						delLocations.getPostCode().toLowerCase().startsWith(postCodeOrSubId.toLowerCase()) 
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
	
	
	
	final Handler mHandler = new Handler();
	final Runnable mUpdateResults = new Runnable() {        
		public void run() {            
			updateResultsInUi();        
		}    
	};
	String serverResponse;
	protected void getDeliveryLocationsOld(final String postCodeOrSubId) {        
		pd = ProgressDialog.show(DeliverySuburbActivity.this, "", "Please wait...", true, false);

		Thread t = new Thread() {            
			public void run() {                
			
				try {
					//calling the auth service
					startLongRunningOperation(postCodeOrSubId);
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
	public void startLongRunningOperation(final String postCodeOrSubId) {
		
//		storeLocations=AppProperties.getCityAllCarersFromCache(cityName);
//		if(null != storeLocations && storeLocations.size()>0)
//			return;
		
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		
		HttpGet httpGet = new HttpGet(AppProperties.WEB_SERVICE_PATH+"/DeliveryLocation.aspx?SubQueryName="+postCodeOrSubId);
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
		String errorMessage="";
		GsonBuilder gsonb = new GsonBuilder();
	      Gson gson = gsonb.create();
	      JSONArray results = new JSONArray(serverResponse);
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
	      
	      deliveryLocationList = new ArrayList<DelLocations>();

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
	      //////////////////////////// LOOOOOOOOOOOOPPPPPPPPPPPPPPP
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
		
	      
			eventsAdapter = new StateListAdapter(this,R.layout.line_item_del, deliveryLocationList);
			eventsAdapter.notifyDataSetChanged();
			listview.setAdapter(eventsAdapter);			
			
			
		} catch (Exception e){
			e.getMessage();
			AlertDialog alertDialog = new AlertDialog.Builder(DeliverySuburbActivity.this).create();
		    alertDialog.setTitle("Login");
		    alertDialog.setMessage("Connection Error");
		    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int which) {
		        return;
		    } }); 
		    alertDialog.show();
		}
		
	}
	
////////////////////////////////////////LIST ADAPTER	
	private class StateListAdapter extends ArrayAdapter<DelLocations> {

		private ArrayList<DelLocations> items;
		 
		
		public StateListAdapter(Context context, int viewResourceId,
				ArrayList<DelLocations> items) {
			super(context, viewResourceId, items);
			this.items = items;
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.line_item_del,null);
			}
			DelLocations event = items.get(position);
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

	protected void getLocationDetails(final String locationId) {
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

	public void populateLocationDetails(
			final String locationId) {

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet;
		HttpResponse response;
		StatusLine statusLine;
		int statusCode;
		LocationDetails locsTemp;

			try {
				httpGet = new HttpGet(AppProperties.WEB_SERVICE_PATH+ "/GetLocationDetail.aspx?LocationID="+ locationId);

				response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();

				serverResponse = Utils.convertStreamToString(content);

				String errorMessage = "";
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

		
		AppProperties.saveLocationObj(DeliverySuburbActivity.this, selectedLocation);
		
//		AppDao dao=null;
//		try {
//			dao=AppDao.getSingleton(getApplicationContext());
//			dao.openConnection();
//		
//			dao.insertLocationHistory(selectedLocation, "True");
//			
//
//		} catch (Exception ex)
//		{
//			System.out.println(ex.getMessage());
//		}finally{
//			if(null!=dao)
//				dao.closeConnection();
//		}
		
		Intent intent = new Intent(DeliverySuburbActivity.this,DeliveryAddress.class);
		Bundle bundle = new Bundle();
		bundle.putString("location",eBean.getSuburbName()+" "+eBean.getPostCode());
		bundle.putString("store",eBean.getLocName());
		bundle.putString("suburbId",eBean.getSuburbID());
		intent.putExtras(bundle);
		startActivityForResult(intent, 56);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////

}
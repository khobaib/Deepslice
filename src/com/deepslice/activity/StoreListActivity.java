package com.deepslice.activity;



import java.io.IOException;
import java.io.InputStream;
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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
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

import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.LocationDetails;
import com.deepslice.model.LocationPoints;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.MyLocation;
import com.deepslice.utilities.MyLocation.LocationResult;
import com.deepslice.utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StoreListActivity extends Activity {
	SharedPreferences settings;
	ImageView addPic;
	ListView listview;
	MyListAdapter myAdapter;	
	static String cityName="";
	static String stName="";
	EditText input_text ;
	MyLocation myLocation = null;
	ProgressDialog pd;
	ImageView searchIcon;
	double currentLongitude,currentLatitude;
	boolean locationFetched=false;
	ArrayList<LocationDetails> searchedList; 
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_location_list);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		listview = (ListView) findViewById(R.id.listView1);
		myLocation = new MyLocation();
		searchedList=new ArrayList<LocationDetails>();
		myAdapter = new MyListAdapter(this,R.layout.line_item, searchedList);
		listview.setAdapter(myAdapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				LocationDetails eBean = (LocationDetails) v.getTag();
				if (eBean != null) {
					AppProperties.saveLocationObj(StoreListActivity.this, eBean);
					
//////////////saving history
					
			        DeepsliceDatabase dbInstance = new DeepsliceDatabase(StoreListActivity.this);
			        dbInstance.open(); 
			        dbInstance.insertLocationHistory(eBean, "False");
			        dbInstance.close();
			        
//					AppDao dao=null;
//					try {
//						dao=AppDao.getSingleton(getApplicationContext());
//						dao.openConnection();
//					
//						dao.insertLocationHistory(eBean, "False");
//						
//
//					} catch (Exception ex)
//					{
//						System.out.println(ex.getMessage());
//					}finally{
//						if(null!=dao)
//							dao.closeConnection();
//					}
//////
					
//					Intent intent;
//					if(AppProperties.isLoogedIn)
//						intent=new Intent(StoreListActivity.this, MenuActivity.class);
//					else
//						intent=new Intent(StoreListActivity.this, CustomerDetailsActivity.class);
	                   Intent intent = new Intent(StoreListActivity.this,DateTimeActivity.class);
	                    Bundle bundle = new Bundle();
	                    bundle.putString("location",eBean.getLocSuburb()+" "+eBean.getLocPostalCode());
	                    bundle.putString("store",eBean.getLocName());
	                    bundle.putString("suburbId",eBean.getLocationID());
	                    intent.putExtras(bundle);
	                    startActivityForResult(intent, 56);
					
//					Bundle bundle = new Bundle();
//					
//					bundle.putInt("position", position);
//					intent.putExtras(bundle);
//					startActivityForResult(intent, 56);
				}
			}
		});

		ImageView mapView= (ImageView)findViewById(R.id.imageView1);
		mapView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(StoreListActivity.this,CityMapActivity.class);
				Bundle bundle = new Bundle();
				bundle.putDouble("cLongi", currentLatitude);
				bundle.putDouble("cLati", currentLongitude);
				intent.putExtras(bundle);
				startActivityForResult(intent, 56);
			}
		});
		

		ImageView emgBack= (ImageView)findViewById(R.id.farwordImageView);
		emgBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		
		 final LocationResult locationResult = new LocationResult(){
			    @Override
			    public void gotLocation(final Location location){
//			    	pd.dismiss();
			    	try{
	    		    if(locationFetched==true)
	    		    	return;
			    		currentLatitude=location.getLatitude();
			    		currentLongitude=location.getLongitude();
			    		
			    		System.out.println(location.getLatitude()+"::"+location.getLongitude());
			    	
	    		    getDeliveryLocations(location.getLatitude(),location.getLongitude());

			    	}catch(Exception e){
				    e.printStackTrace();	
				    
				    }

			        }
			    	
			};
			pd = ProgressDialog.show(StoreListActivity.this, "", "Searching...", true, false);
			locationFetched=false;
			myLocation.getLocation(StoreListActivity.this, locationResult);
	}
	
	protected void getDeliveryLocations(double myLati,double myLongi) {
		locationFetched=true;
 
		try{
		
			List<LocationPoints> locList = AppProperties.locationPointsList;
			
			
			ArrayList<LocationPoints> selectedList = new ArrayList<LocationPoints>();

			
			double thisLati,thisLongi;
			for (LocationPoints lPoint : locList) {
				
				thisLati=Double.parseDouble(lPoint.getLatitude());
				thisLongi=Double.parseDouble(lPoint.getLongitude());
				float[] results= new float[1];
				Location.distanceBetween(myLati, myLongi, thisLati, thisLongi, results);
				float dist=0F;
				if(results.length==3)
					dist=results[2];
				if(results.length==2)
					dist=results[1];
				if(results.length==1)
					dist=results[0];
				
	        	 System.out.println(dist);
	        	 lPoint.setDistance(dist);
	        	 if(dist <= 10000*1000)
	        		 selectedList.add(lPoint);
				
				if(selectedList.size()>=10)
					break;
			}

			
			System.out.println("====>>>>> "+selectedList.size());
			
			getPickupLocations(selectedList);
			
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////

	final Handler mHandler = new Handler();
	final Runnable mUpdateResults = new Runnable() {        
		public void run() {            
			updateResultsInUi();        
		}    
	};
	String serverResponseDLocs;
	protected void getPickupLocations(final ArrayList<LocationPoints> selectedList) {        

		Thread t = new Thread() {            
			public void run() {                
			
				try {
					//calling the auth service
					
					populatePickupLocations(selectedList);	
					
				} catch (Exception ex)
				{
					System.out.println(ex.getMessage());
				}
				mHandler.post(mUpdateResults);            
			}

			
		};        
		t.start();    	
	}
	
	public void populatePickupLocations(final ArrayList<LocationPoints> selectedList) {

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet;
		HttpResponse response;
		StatusLine statusLine;
		int statusCode;
		LocationDetails locsTemp;
		searchedList=new ArrayList<LocationDetails>();
		for (LocationPoints locationPoints : selectedList) {
			
			try {
				httpGet = new HttpGet(Constants.ROOT_URL+"/GetLocationDetail.aspx?LocationID="+locationPoints.getLocationID());
			//	httpGet = new HttpGet(AppProperties.WEB_SERVICE_PATH+"/GetLocationDetail.aspx?LocationID="+"02");
			//	httpGet =new HttpGet(AppProperties.WEB_SERVICE_PATH+"/GetLocationPoints.aspx");
				
				response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				Log.e("Response from Json",response.toString());
				
				serverResponseDLocs=Utils.convertStreamToString(content);
				
				String errorMessage="";
				GsonBuilder gsonb = new GsonBuilder();
			      Gson gson = gsonb.create();
			      JSONArray results = new JSONArray(serverResponseDLocs);
			      JSONObject respOuter = results.getJSONObject(0);
			      JSONObject resp = respOuter.getJSONObject("Response");

			      JSONObject errors = resp.getJSONObject("Errors");
			      boolean hasError=errors.has("Message");
			      if(hasError)
			    	  {
			    	  continue;
			    	  }

			      
			      JSONArray resultsArray =null;
			      Object data= resp.get("Data");
			      boolean dataExists=false;
			      if(data instanceof JSONArray)
			    	  {
			    	  resultsArray =(JSONArray)data;
			    	  dataExists=true;
			    	  }
			      
			      
			      LocationDetails locs= new LocationDetails();
		
			      if(dataExists==true)
			      {
			    	  LocationDetails aBean;
				      for(int i=0; i<resultsArray.length(); i++){
				    	  JSONObject jsResult = resultsArray.getJSONObject(i);
				          if(jsResult!=null){
				                String jsonString = jsResult.toString();
				                aBean=new LocationDetails();
				                aBean=gson.fromJson(jsonString, LocationDetails.class);
				//                System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
				                searchedList.add(aBean);
				          }
				     }
			      }
		
	      AppProperties.locationPointsSearched=searchedList;
	      System.out.println("Got delivery locations: "+searchedList.size());
	      
	      //////////////////////////// LOOOOOOOOOOOOPPPPPPPPPPPPPPP
		//////////////////////////////////////////////////////////
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			
		e.printStackTrace();
	}
	
	}
	
	}
	
	private void updateResultsInUi() { 
		pd.dismiss();
		myAdapter = new MyListAdapter(this,R.layout.line_item, searchedList);
	      listview.setAdapter(myAdapter);
	      myAdapter.notifyDataSetChanged();
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	private class MyListAdapter extends ArrayAdapter<LocationDetails> {

		private ArrayList<LocationDetails> items;
		 
		
		public MyListAdapter(Context context, int viewResourceId,
				ArrayList<LocationDetails> items) {
			super(context, viewResourceId, items);
			this.items = items;
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.line_item,null);
			}
			LocationDetails event = items.get(position);
			if (event != null) {
				
				TextView title = (TextView) convertView.findViewById(R.id.textView1);
				TextView sub = (TextView) convertView.findViewById(R.id.textView2);
				TextView sub2 = (TextView) convertView.findViewById(R.id.textView3);
				
				title.setText(event.getLocName());
				sub.setText(event.getLocSuburb()+" "+event.getLocPostalCode());
				sub2.setText(event.getLocStreet() +" "+event.getLocAddress());

				convertView.setTag(event);
			}
			return convertView;
		}

	}
///////////////////////// END LIST ADAPTER	
	
}
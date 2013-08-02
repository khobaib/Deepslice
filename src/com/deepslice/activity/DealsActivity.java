package com.deepslice.activity;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.deepslice.cache.ImageLoader;
import com.deepslice.database.AppDao;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.vo.CouponDetailsVo;
import com.deepslice.vo.CouponsVo;
import com.deepslice.vo.DealOrderVo;
import com.deepslice.vo.LocationDetails;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DealsActivity extends Activity {
	SharedPreferences settings;
	ProgressDialog pd;
	ImageView searchIcon;
	 CouponsVo couponsVo;
	 String locationId="0";
	 ArrayList<CouponsVo> couponsList;
	 ListView listview;
	 MyListAdapterFav myAdapter;
    public ImageLoader imageLoader;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deals_list);
        imageLoader=new ImageLoader(this);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		listview = (ListView) findViewById(R.id.listView1);
		couponsList=new ArrayList<CouponsVo>();
		myAdapter = new MyListAdapterFav(this,R.layout.deal_item, couponsList);
		listview.setAdapter(myAdapter);

		
		LocationDetails locationObj = AppProperties.getLocationObj(DealsActivity.this);
		locationId=locationObj.getLocationID();
        AppDao dao=null;
        try {
            dao=AppDao.getSingleton(getApplicationContext());
            dao.openConnection();
            ArrayList<CouponsVo>temp=dao.getDealList();
            if (temp.size()>0){
                myAdapter = new MyListAdapterFav(this,R.layout.line_item_del, temp);
                listview.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            }   else {
		        getCouponInfo();
            }
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }finally{
            if(null!=dao)
                dao.closeConnection();
        }
		Button openFavs=(Button)findViewById(R.id.favs);
		openFavs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(DealsActivity.this,FavsListActivity.class);
				startActivity(intent);
				
			}
		});
		
		Button mainMenu=(Button)findViewById(R.id.mainMenu);
		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(DealsActivity.this,MenuActivity.class);
				startActivity(intent);
				
			}
		});
		LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
		myOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(DealsActivity.this,MyOrderActivity.class);
				startActivity(intent);
				
			}
		});	

		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				CouponsVo couponsVo = (CouponsVo) v.getTag();
				if (couponsVo != null) {
					
//					getCouponDetails(couponsVo.getCouponID());
                    AppSharedPreference.putBoolean(DealsActivity.this, couponsVo.getCouponID() + "", false);
                    removeUnfinishedDeals();
					Intent i=new Intent(DealsActivity.this, DealsListActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("couponId",couponsVo.getCouponID());
					bundle.putSerializable("couponDesc",couponsVo.getCouponDesc());
                    bundle.putSerializable("couponsVo",couponsVo);
					i.putExtras(bundle);
					startActivityForResult(i, 112233);
				}
			}
		});
					
	}
    private void removeUnfinishedDeals(){
        AppDao dao = null;
        try {
            dao = AppDao.getSingleton(getApplicationContext());
            dao.openConnection();
            int x=dao.getDealOrdersList().size();

            dao.deleteUnfinishedDealOrderRec();
            int y=dao.getDealOrdersList().size();
            //Toast.makeText(DealsActivity.this,"cou"+x+"gr"+y,Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (null != dao)
                dao.closeConnection();
        }
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
		pd = ProgressDialog.show(DealsActivity.this, "", "Please wait...", true, false);

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
//comment = new api implementation to get deal coupons
		HttpGet httpGet = new HttpGet(AppProperties.WEB_SERVICE_PATH+"/GetCoupons.aspx?CouponCode=0&Filter=Deals");
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


			//////////////////////////////////////////////////////////
			String errorMessage="";
			GsonBuilder gsonb = new GsonBuilder();
		      Gson gson = gsonb.create();
		      JSONArray results = new JSONArray(serverResponse);
		      JSONObject respOuter = results.getJSONObject(0);
		      JSONObject resp = respOuter.getJSONObject("Response");
//		      String status = resp.getString("Status");
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
		      
		      couponsList = new ArrayList<CouponsVo>();

		      if(dataExists==true)
		      {
		    	  CouponsVo aBean;
                  //db data enter
                  AppDao dao=null;
                  try {
                      dao=AppDao.getSingleton(getApplicationContext());
                      dao.openConnection();
			      for(int i=0; i<resultsArray.length(); i++){
			    	  JSONObject jsResult = resultsArray.getJSONObject(i);
			          if(jsResult!=null){
			                String jsonString = jsResult.toString();
			                aBean=new CouponsVo();
			                aBean=gson.fromJson(jsonString, CouponsVo.class);
			//                System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
			                couponsList.add(aBean);
                            dao.insertDeals(aBean);
			          }
			     }
                  } catch (Exception ex)
                  {
                      System.out.println(ex.getMessage());
                  }finally{
                      if(null!=dao)
                          dao.closeConnection();
                  }
		      }
		      
				myAdapter = new MyListAdapterFav(this,R.layout.line_item_del, couponsList);
				listview.setAdapter(myAdapter);
				myAdapter.notifyDataSetChanged();
				
		      System.out.println("Got location points: "+couponsList.size());
			
		} catch (Exception e){
			e.printStackTrace();
			AlertDialog alertDialog = new AlertDialog.Builder(DealsActivity.this).create();
		    alertDialog.setTitle("info");
		    alertDialog.setMessage("Try again later");
		    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int which) {

                  DealsActivity.this.finish();
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
		pd = ProgressDialog.show(DealsActivity.this, "", "Please wait...", true, false);

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
		
		HttpGet httpGet = new HttpGet(AppProperties.WEB_SERVICE_PATH+"/GetCouponDetail.aspx?CouponID="+couponId);
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
	      
	      ArrayList<CouponDetailsVo> couponDetails = new ArrayList<CouponDetailsVo>();

	      if(dataExists==true)
	      {
	    	  CouponDetailsVo aBean;
		      for(int i=0; i<resultsArray.length(); i++){
		    	  JSONObject jsResult = resultsArray.getJSONObject(i);
		          if(jsResult!=null){
		                String jsonString = jsResult.toString();
		                aBean=new CouponDetailsVo();
		                aBean=gson.fromJson(jsonString, CouponDetailsVo.class);
		//                System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
		                couponDetails.add(aBean);
		          }
		     }
	      }
	      System.out.println(couponDetails.size());

	      AppDao dao=null;
			try {
				dao=AppDao.getSingleton(getApplicationContext());
				dao.openConnection();
				
				dao.updateOrderDetails(couponDetails);
				
			} catch (Exception ex)
			{
				System.out.println(ex.getMessage());
			}finally{
				if(null!=dao)
					dao.closeConnection();
			}
			
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
		
			
		} catch (Exception e){
			e.getMessage();
			AlertDialog alertDialog = new AlertDialog.Builder(DealsActivity.this).create();
		    alertDialog.setTitle("Add Coupon");
		    alertDialog.setMessage(e.getMessage());
		    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int which) {
		        return;
		    } }); 
		    alertDialog.show();
		}
		
	}



	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////

	private class MyListAdapterFav extends ArrayAdapter<CouponsVo> {

		private ArrayList<CouponsVo> items;

		public MyListAdapterFav(Context context, int viewResourceId,
				ArrayList<CouponsVo> items) {
			super(context, viewResourceId, items);
			this.items = items;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.deal_item, null);
			}
			CouponsVo event = items.get(position);
			if (event != null) {
				TextView title = (TextView) convertView.findViewById(R.id.textView1);
				title.setText(event.getCouponDesc());
                ImageView icon = (ImageView) convertView.findViewById(R.id.imageView1);
                String imgPath=AppProperties.IMAGES_LOCATION;
                if(AppProperties.isNull(event.getPic())){
                    imgPath=imgPath+"noimage.png";
                }
                else{
                    imgPath=imgPath+event.getPic();
                }
                imageLoader.DisplayImage(imgPath,DealsActivity.this, icon);
                System.out.println(",,,,,,,,,,,,,,,,,,"+imgPath);
				convertView.setTag(event);
			}
			return convertView;
		}

	}
	////////////////////////// END LIST ADAPTER

	@Override
	protected void onResume() {
		// //////////////////////////////////////////////////////////////////////////////
        AppDao dao = null;
        try {
            dao = AppDao.getSingleton(getApplicationContext());
            dao.openConnection();
            ArrayList<String> orderInfo = dao.getOrderInfo();
            ArrayList<DealOrderVo>dealOrderVos1= dao.getDealOrdersList();
            TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
            double tota=0.00;
            int dealCount=0;
            if((dealOrderVos1!=null && dealOrderVos1.size()>0)){
                dealCount=dealOrderVos1.size();
                for (int x=0;x<dealOrderVos1.size();x++){
                    tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
                }
            }

            int orderInfoCount= 0;
            double  orderInfoTotal=0.0;
            if ((null != orderInfo && orderInfo.size() == 2) ) {
                orderInfoCount=Integer.parseInt(orderInfo.get(0));
                orderInfoTotal=Double.parseDouble(orderInfo.get(1));
            }
            int numPro=orderInfoCount+dealCount;
            double subTotal=orderInfoTotal+tota;
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            subTotal= Double.valueOf(twoDForm.format(subTotal));
            if(numPro>0){
                itemsPrice.setText(numPro+" Items "+"\n$" +subTotal );
                itemsPrice.setVisibility(View.VISIBLE);
            }

            else{
                itemsPrice.setVisibility(View.INVISIBLE);

            }

            TextView favCount = (TextView) findViewById(R.id.favCount);
            String fvs=dao.getFavCount();
            if (null != fvs && !fvs.equals("0")) {
                favCount.setText(fvs);
                favCount.setVisibility(View.VISIBLE);
            }
            else{
                favCount.setVisibility(View.INVISIBLE);
            }



        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (null != dao)
                dao.closeConnection();
        }
        // ///////////////////////////////////////////////////////////////////////

        // ///////////////////////////////////////////////////////////////////////

		super.onResume();
	}
	
}
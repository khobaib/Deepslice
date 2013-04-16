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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.deepslice.cache.ImageLoader;
import com.deepslice.database.AppDao;
import com.deepslice.utilities.AppProperties;
import com.deepslice.vo.AllProductsVo;
import com.deepslice.vo.CouponDetailsVo;
import com.deepslice.vo.OrderVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DealsListActivity extends Activity{
	
	ArrayList<AllProductsVo> allProductsList;

	ListView listview;
	MyListAdapterFav myAdapter;
	
	ProgressDialog pd;
	String couponId,couponDesc;
	public ImageLoader imageLoader;
	AllProductsVo selectedBean;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fav_list);
		imageLoader=new ImageLoader(this);	
		Bundle b = this.getIntent().getExtras();
		couponId=b.getString("couponId");
		couponDesc=b.getString("couponDesc");
		
		TextView title = (TextView) findViewById(R.id.headerTextView);
		title.setText(couponDesc);

		getCouponDetails(couponId);
		
		Button openFavs=(Button)findViewById(R.id.favs);
		openFavs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(DealsListActivity.this,FavsListActivity.class);
				startActivity(intent);
				
			}
		});
		
		allProductsList=new ArrayList<AllProductsVo>();
		listview = (ListView) findViewById(R.id.listView1);				
		myAdapter = new MyListAdapterFav(this,R.layout.line_item_product, allProductsList);
		listview.setAdapter(myAdapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				AllProductsVo eBean = (AllProductsVo) v.getTag();
				if (eBean != null) {
					selectedBean = eBean;
					
					String prodType=AppProperties.getCatName(DealsListActivity.this, eBean.getProdCatID());
					
					AppDao dao=null;
					try {
						dao=AppDao.getSingleton(getApplicationContext());
						dao.openConnection();
						
							dao.insertOrder(getOrderBean(eBean,prodType));
							
							Toast.makeText(DealsListActivity.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();
							
					} catch (Exception ex)
					{
						System.out.println(ex.getMessage());
					}finally{
						if(null!=dao)
							dao.closeConnection();
					}
					doResumeWork();
				}
				
			}
		});
		
		
		Button mainMenu=(Button)findViewById(R.id.mainMenu);
		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(DealsListActivity.this,MenuActivity.class);
				startActivity(intent);
				
			}
		});
		
		LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
		myOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(DealsListActivity.this,MyOrderActivity.class);
				startActivity(intent);
				
			}
		});
	}
private OrderVo getOrderBean(AllProductsVo prodBean, String catType) {
	
	OrderVo f = new OrderVo();
    	f.setProdCatID(prodBean.getProdCatID());
    	f.setSubCatID1(prodBean.getSubCatID1());
    	f.setSubCatID2(prodBean.getSubCatID2());
        f.setProdID(prodBean.getProdID());
        f.setProdCode(prodBean.getProdCode());
        f.setDisplayName(prodBean.getDisplayName());
        f.setProdAbbr(prodBean.getProdAbbr());
        f.setProdDesc(prodBean.getProdDesc());
        f.setIsAddDeliveryAmount(prodBean.getIsAddDeliveryAmount());
        f.setDisplaySequence(prodBean.getDisplaySequence());
        f.setCaloriesQty(prodBean.getCaloriesQty());
        
        double finalPrice=Double.parseDouble(prodBean.getPrice());
        finalPrice=AppProperties.roundTwoDecimals(finalPrice);
        f.setPrice(String.valueOf(finalPrice));
        
        f.setThumbnail(prodBean.getThumbnail());
        f.setFullImage(prodBean.getFullImage());
        
        f.setQuantity(String.valueOf(1));
        
        f.setCrust("");
        f.setSauce("");
        f.setToppings("");
		
        f.setProdCatName(catType);
        
        return f;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////

	private class MyListAdapterFav extends ArrayAdapter<AllProductsVo> {

		private ArrayList<AllProductsVo> items;

		public MyListAdapterFav(Context context, int viewResourceId,
				ArrayList<AllProductsVo> items) {
			super(context, viewResourceId, items);
			this.items = items;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.line_item_product, null);
			}
			AllProductsVo event = items.get(position);
			if (event != null) {

				TextView title = (TextView) convertView.findViewById(R.id.textView1);
				TextView price = (TextView) convertView.findViewById(R.id.textView2);
				TextView calories = (TextView) convertView.findViewById(R.id.textView3);

				title.setText(event.getDisplayName());
				price.setText("$"+AppProperties.getRoundTwoDecimalString(event.getPrice()));
				calories.setText(event.getCaloriesQty()+"kj");

				
				ImageView icon = (ImageView) convertView.findViewById(R.id.imageView1);
				String imgPath=AppProperties.IMAGES_LOCATION;
				if(AppProperties.isNull(event.getThumbnail())){
					imgPath=imgPath+"noimage.png";
				}
				else{
					imgPath=imgPath+event.getThumbnail();
				}
				imageLoader.DisplayImage(imgPath,DealsListActivity.this, icon);
				
				convertView.setTag(event);
			}
			return convertView;
		}

	}
	////////////////////////// END LIST ADAPTER
	//===========================================================================================
	//===========================================================================================
	//===========================================================================================
	

@Override
protected void onResume() {
	doResumeWork();

	super.onResume();
}

private void doResumeWork() {
	// //////////////////////////////////////////////////////////////////////////////
		AppDao dao = null;
		try {
			dao = AppDao.getSingleton(getApplicationContext());
			dao.openConnection();

			ArrayList<String> orderInfo = dao.getOrderInfo();
			
			TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
			if (null != orderInfo && orderInfo.size() == 2) {
				itemsPrice.setText(orderInfo.get(0)+" Items "+"\n$" + orderInfo.get(1));
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
		
	}

final Handler mHandlerDetails = new Handler();
final Runnable mUpdateResultsDetails = new Runnable() {        
	public void run() {            
		updateResultsInUiDetails();        
	}    
};
String serverResponseDetails;
String delLocError="";
protected void getCouponDetails(final String couponId) {        
	pd = ProgressDialog.show(DealsListActivity.this, "", "Please wait...", true, false);

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
			allProductsList=new ArrayList<AllProductsVo>();
			AllProductsVo tempProd=null;
		      for (CouponDetailsVo couponDetailsVo : couponDetails) {
		    	  	tempProd = dao.getProductById(couponDetailsVo.getProdID());
		    	  	if(tempProd != null)
		    	  	{
		    	  		tempProd.setPrice(couponDetailsVo.getDiscountedPrice());
		    	  		allProductsList.add(tempProd);
		    	  	}
		      }
			
			
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
		
		listview = (ListView) findViewById(R.id.listView1);				
		myAdapter = new MyListAdapterFav(this,R.layout.line_item_product, allProductsList);
		listview.setAdapter(myAdapter);
		myAdapter.notifyDataSetChanged();
		
	} catch (Exception e){
		e.printStackTrace();
	}
	
}



// ////////////////////////////////////////////////////////////////////////////////////////////
// ////////////////////////////////////////////////////////////////////////////////////////////
// ////////////////////////////////////////////////////////////////////////////////////////////
// ////////////////////////////////////////////////////////////////////////////////////////////

}

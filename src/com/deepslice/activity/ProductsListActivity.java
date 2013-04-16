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
import android.text.Html;
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

import com.deepslice.cache.ImageLoader;
import com.deepslice.database.AppDao;
import com.deepslice.utilities.AppProperties;
import com.deepslice.vo.AllProductsVo;
import com.deepslice.vo.ToppingPricesVo;
import com.deepslice.vo.ToppingSizesVo;
import com.deepslice.vo.ToppingsAndSaucesVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ProductsListActivity extends Activity{
	
	ArrayList<AllProductsVo> allProductsList;

	ListView listview;
	MyListAdapterProd myAdapter;
	
	String catType;
	String catId;
	String subCatId;

	ProgressDialog pd;
	boolean syncedPrices =false;
	boolean syncedToppings =false;
	
	public ImageLoader imageLoader;
	AllProductsVo selectedBean;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_menu_products);
		imageLoader=new ImageLoader(this);	
		Bundle b = this.getIntent().getExtras();
		
		catId=b.getString("catId");
		subCatId=b.getString("subCatId"); 
		catType=b.getString("catType");
		
		String titeDisplay=b.getString("titeDisplay");
		
		TextView title = (TextView) findViewById(R.id.headerTextView);
		

		if(AppProperties.isNull(titeDisplay))
		{
			title.setText(catType);	
		}
		else{
			title.setText(titeDisplay);
		}
		
	      AppDao dao=null;
				try {
					dao=AppDao.getSingleton(getApplicationContext());
					dao.openConnection();
				
					if("Pizza".equals(catType)){
						allProductsList=dao.getProductsPizza(catId,subCatId);
					}
					else
					{
					allProductsList=dao.getProductsSelected(catId,subCatId);
					}
					
				} catch (Exception ex)
				{
					System.out.println(ex.getMessage());
				}finally{
					if(null!=dao)
						dao.closeConnection();
				}
		
				listview = (ListView) findViewById(R.id.listView1);				
				myAdapter = new MyListAdapterProd(this,R.layout.line_item_product, allProductsList);
				listview.setAdapter(myAdapter);
				
				listview.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v, int position,
							long id) {
						AllProductsVo eBean = (AllProductsVo) v.getTag();
						if (eBean != null) {
							selectedBean = eBean;
							
							if("Pizza".equals(catType)){
								updateTopingSaucesData(eBean.getProdID());
//								Utils.openErrorDialog(ProductsListActivity.this, eBean.getProdID());
							}
							else{
							
								Intent i=new Intent(ProductsListActivity.this, FavAddActivity.class);
								Bundle bundle=new Bundle();
								bundle.putString("itemName",eBean.getDisplayName());
								bundle.putSerializable("prodBean",eBean);
								bundle.putString("catType",catType);
								i.putExtras(bundle);
								startActivityForResult(i, 112233);
							}
						}
					}
				});
				
				Button openFavs=(Button)findViewById(R.id.favs);
				openFavs.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						Intent intent=new Intent(ProductsListActivity.this,FavsListActivity.class);
						startActivity(intent);
						
					}
				});
				
				Button mainMenu=(Button)findViewById(R.id.mainMenu);
				mainMenu.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						Intent intent=new Intent(ProductsListActivity.this,MenuActivity.class);
						startActivity(intent);
						
					}
				});
				LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
				myOrder.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						Intent intent=new Intent(ProductsListActivity.this,MyOrderActivity.class);
						startActivity(intent);
						
					}
				});	
		}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////

	private class MyListAdapterProd extends ArrayAdapter<AllProductsVo> {

		private ArrayList<AllProductsVo> items;

		public MyListAdapterProd(Context context, int viewResourceId,
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

				title.setText(Html.fromHtml(event.getDisplayName()));
				price.setText("$"+event.getPrice());
				calories.setText(event.getCaloriesQty()+"kj");

				
				ImageView icon = (ImageView) convertView.findViewById(R.id.imageView1);
				String imgPath=AppProperties.IMAGES_LOCATION;
				if(AppProperties.isNull(event.getThumbnail())){
					imgPath=imgPath+"noimage.png";
				}
				else{
					imgPath=imgPath+event.getThumbnail();
				}
				imageLoader.DisplayImage(imgPath,ProductsListActivity.this, icon);
				
				convertView.setTag(event);
			}
			return convertView;
		}

	}
	////////////////////////// END LIST ADAPTER
	//===========================================================================================
	//===========================================================================================
	//===========================================================================================
	

	final Handler mHandler = new Handler();
	final Runnable mUpdateResults = new Runnable() {        
		public void run() {            
			updateResultsInUi();        
		}    
	};
	String serverResponse;
	protected void updateTopingSaucesData(final String prodId) {
		
		
		AppDao dao=null;
		try {
			dao=AppDao.getSingleton(getApplicationContext());
			dao.openConnection();
			
			syncedPrices=dao.recordExistsToppingPrices();
			syncedToppings=dao.recordExistsToppings(prodId);
	
//			dao.insertOrUpdateList(questionList);
			
		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}finally{
			if(null!=dao)
				dao.closeConnection();
		}

	if(syncedPrices && syncedToppings)
	{
	updateResultsInUi();
	}
	else
	{
		pd = ProgressDialog.show(ProductsListActivity.this, "", "Please wait...", true, false);

		Thread t = new Thread() {            
			public void run() {                
			
				try {
					
					if(syncedToppings==false)
						populateToppingsAndSauces(prodId);
					
					if(syncedPrices==false)
					{
						populateToppingSizes();
						populateToppingPrices();
					}
					
				} catch (Exception ex)
				{
					System.out.println(ex.getMessage());
				}
				mHandler.post(mUpdateResults);            
			}

			
		};        
		t.start();
	}
		
	}

	private String getProdCatId(String abbr) {
		String pCatId="0";
		AppDao dao=null;
		try {
			dao=AppDao.getSingleton(getApplicationContext());
			dao.openConnection();
			
			pCatId=dao.getCatIdByCatCode(abbr);
	
		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}finally{
			if(null!=dao)
				dao.closeConnection();
		}

		return pCatId;
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void populateToppingsAndSauces(String prodId) {

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		
		HttpGet httpGet = new HttpGet(AppProperties.WEB_SERVICE_PATH+"/GetPizzaToppingsAndSauces.aspx?prodID="+prodId);
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
	      
	      ArrayList<ToppingsAndSaucesVo> pCatList = new ArrayList<ToppingsAndSaucesVo>();

	      if(dataExists==true)
	      {
	    	  ToppingsAndSaucesVo aBean;
		      for(int i=0; i<resultsArray.length(); i++){
		    	  JSONObject jsResult = resultsArray.getJSONObject(i);
		          if(jsResult!=null){
		                String jsonString = jsResult.toString();
		                aBean=new ToppingsAndSaucesVo();
		                aBean=gson.fromJson(jsonString, ToppingsAndSaucesVo.class);
		//                System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
		                pCatList.add(aBean);
		                
		          }
		     }
	      }
	      
	      AppDao dao=null;
			try {
				dao=AppDao.getSingleton(getApplicationContext());
				dao.openConnection();
				
				dao.insertToppingSauces(pCatList);
				
			} catch (Exception ex)
			{
				System.out.println(ex.getMessage());
			}finally{
				if(null!=dao)
					dao.closeConnection();
			}
	      
	      System.out.println("Got Toppings And Sauces: "+pCatList.size());
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void populateToppingSizes() {

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();

		HttpGet httpGet = new HttpGet(AppProperties.WEB_SERVICE_PATH + "/GetToppingSizes.aspx");
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

			// ////////////////////////////////////////////////////////
			String errorMessage = "";
			GsonBuilder gsonb = new GsonBuilder();
			Gson gson = gsonb.create();
			JSONArray results = new JSONArray(serverResponse);
			JSONObject respOuter = results.getJSONObject(0);
			JSONObject resp = respOuter.getJSONObject("Response");
			String status = resp.getString("Status");
			JSONArray resultsArray = null;
			Object data = resp.get("Data");
			boolean dataExists = false;
			if (data instanceof JSONArray) {
				resultsArray = (JSONArray) data;
				dataExists = true;
			}

			JSONObject errors = resp.getJSONObject("Errors");

			boolean hasError = errors.has("Message");
			if (hasError) {
				errorMessage = errors.getString("Message");
				System.out.println("Error:" + errorMessage);
			}

			ArrayList<ToppingSizesVo> pCatList = new ArrayList<ToppingSizesVo>();

			if (dataExists == true) {
				ToppingSizesVo aBean;
				for (int i = 0; i < resultsArray.length(); i++) {
					JSONObject jsResult = resultsArray.getJSONObject(i);
					if (jsResult != null) {
						String jsonString = jsResult.toString();
						aBean = new ToppingSizesVo();
						aBean = gson
								.fromJson(jsonString, ToppingSizesVo.class);
						// System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
						pCatList.add(aBean);
					}
				}
			}

//			AppProperties.subCatList = pCatList;
		      AppDao dao=null;
				try {
					dao=AppDao.getSingleton(getApplicationContext());
					dao.openConnection();
					
					dao.insertToppingSizes(pCatList);
					
				} catch (Exception ex)
				{
					System.out.println(ex.getMessage());
				}finally{
					if(null!=dao)
						dao.closeConnection();
				}

			System.out.println("Got Topping Sizes : " + pCatList.size());
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void populateToppingPrices() {

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();

		HttpGet httpGet = new HttpGet(AppProperties.WEB_SERVICE_PATH + "/GetToppingPrices.aspx");
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

			// ////////////////////////////////////////////////////////
			String errorMessage = "";
			GsonBuilder gsonb = new GsonBuilder();
			Gson gson = gsonb.create();
			JSONArray results = new JSONArray(serverResponse);
			JSONObject respOuter = results.getJSONObject(0);
			JSONObject resp = respOuter.getJSONObject("Response");
			String status = resp.getString("Status");
			JSONArray resultsArray = null;
			Object data = resp.get("Data");
			boolean dataExists = false;
			if (data instanceof JSONArray) {
				resultsArray = (JSONArray) data;
				dataExists = true;
			}

			JSONObject errors = resp.getJSONObject("Errors");

			boolean hasError = errors.has("Message");
			if (hasError) {
				errorMessage = errors.getString("Message");
				System.out.println("Error:" + errorMessage);
			}

			ArrayList<ToppingPricesVo> pCatList = new ArrayList<ToppingPricesVo>();

			if (dataExists == true) {
				ToppingPricesVo aBean;
				for (int i = 0; i < resultsArray.length(); i++) {
					JSONObject jsResult = resultsArray.getJSONObject(i);
					if (jsResult != null) {
						String jsonString = jsResult.toString();
						aBean = new ToppingPricesVo();
						aBean = gson
								.fromJson(jsonString, ToppingPricesVo.class);
						// System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
						pCatList.add(aBean);
					}
				}
			}

//			AppProperties.allProductsList = pCatList;
		      AppDao dao=null;
				try {
					dao=AppDao.getSingleton(getApplicationContext());
					dao.openConnection();
					
					dao.insertToppingPrices(pCatList);
					
				} catch (Exception ex)
				{
					System.out.println(ex.getMessage());
				}finally{
					if(null!=dao)
						dao.closeConnection();
				}

			System.out.println("Got product catetgories: " + pCatList.size());
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
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private void updateResultsInUi() { 

		if(null != pd)
			pd.dismiss();

		Intent i=new Intent(ProductsListActivity.this, PizzaDetailsActivity.class);
		Bundle bundle=new Bundle();
		bundle.putSerializable("selectedProduct",selectedBean);
		i.putExtras(bundle);
		startActivityForResult(i, 112233);
	}	
	@Override
	protected void onResume() {
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

		super.onResume();
	}
}

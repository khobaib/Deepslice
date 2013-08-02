package com.deepslice.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.deepslice.cache.ImageLoader;
import com.deepslice.database.AppDao;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.vo.*;
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

public class DealsListActivity extends Activity{
	
	ArrayList<CouponGroupsVo> allProductsList;

	ListView listview;
	MyListAdapterFav myAdapter;
	
	ProgressDialog pd;
	String couponId,couponDesc,productCatId="0",prodCatCode;
	public ImageLoader imageLoader;
    int numOfDeals=0;
	AllProductsVo selectedBean;
    CouponsVo couponsVo;
   ArrayList<String> couponsId;
    ArrayList<ArrayList<String>> arrayListProductName;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fav_list);
		imageLoader=new ImageLoader(this);	
		Bundle b = this.getIntent().getExtras();
		couponId=b.getString("couponId");
		couponDesc=b.getString("couponDesc");
        couponsVo=(CouponsVo)b.getSerializable("couponsVo");
		TextView title = (TextView) findViewById(R.id.headerTextView);
		title.setText(couponDesc);
        couponsId=new ArrayList<String>();
        arrayListProductName=new ArrayList<ArrayList<String>>();
		getCouponDetails(couponId);
		
		Button openFavs=(Button)findViewById(R.id.favs);
		openFavs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(DealsListActivity.this,FavsListActivity.class);
				startActivity(intent);
				
			}
		});
		
		allProductsList=new ArrayList<CouponGroupsVo>();
		listview = (ListView) findViewById(R.id.listView1);				

		
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
                CouponGroupsVo eBean = (CouponGroupsVo) v.getTag();
				if (eBean != null) {

                   //rukshan add
                    TextView title=(TextView)v.findViewById(R.id.textView1);
                    Intent i=new Intent(DealsListActivity.this,DealGroup.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("couponId",couponId);
                    bundle.putString("couponGroupID",eBean.getCouponGroupID());
                    bundle.putString("Qty",eBean.getQty());
                    bundle.putSerializable("couponsVo", couponsVo);
                    bundle.putStringArrayList("couponsID",couponsId);
                    bundle.putString("title",title.getText().toString());
                   // Toast.makeText(DealsListActivity.this,"cou"+couponId+"gr"+eBean.getCouponGroupID(),Toast.LENGTH_SHORT).show();
                    i.putExtras(bundle);
                    startActivity(i);


					//comment by rukshan
//					String prodType=AppProperties.getCatName(DealsListActivity.this, eBean.getProdCatID());
//
//					AppDao dao=null;
//					try {
//						dao=AppDao.getSingleton(getApplicationContext());
//
//						dao.openConnection();
//
//							dao.insertOrder(getOrderBean(eBean,prodType));
//
//							Toast.makeText(DealsListActivity.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();
//
//					} catch (Exception ex)
//					{
//						System.out.println(ex.getMessage());
//					}finally{
//						if(null!=dao)
//							dao.closeConnection();
//					}
//					doResumeWork();
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

        Button buttonCancel=(Button)findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDao dao = null;
                try {
                    dao = AppDao.getSingleton(getApplicationContext());
                    dao.openConnection();
                  if(dao.deleteDealOrderRec(couponId)){
                    Toast.makeText(DealsListActivity.this, "successfully canceled", Toast.LENGTH_LONG).show();
                    doResumeWork();
                  }else {
                      Toast.makeText(DealsListActivity.this, "no data", Toast.LENGTH_LONG).show();
                  }

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    if (null != dao)
                        dao.closeConnection();
                }
            }
        });
  //comment = add new image for GET A DEAL
        RelativeLayout buttonGetADeal=(RelativeLayout)findViewById(R.id.getDeal);
        buttonGetADeal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for (int x=0;x<couponsId.size();x++){
                    if(AppSharedPreference.getBoolean(DealsListActivity.this,couponsId.get(x))){
                        count++;
                    }
                }
                if (count==couponsId.size()){
                    AppDao dao=null;
                    try {
                        dao=AppDao.getSingleton(getApplicationContext());
                        dao.openConnection();
                        dao.updateDealOrder();
                        Toast.makeText(DealsListActivity.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();

                        ArrayList<DealOrderVo>dealOrderVos1= dao.getDealOrdersList();
                        int i=dealOrderVos1.size();
                        double tota=0.00;
                        for (int x=0;x<dealOrderVos1.size();x++){
                            tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
                        }
                        TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
                        if (null != dealOrderVos1 && dealOrderVos1.size() > 0) {
                            itemsPrice.setText(i+" Items "+"\n$" + tota);
                            itemsPrice.setVisibility(View.VISIBLE);
                        }
                        else{
                            itemsPrice.setVisibility(View.INVISIBLE);

                        }
                        AppSharedPreference.clearDealCoupon(DealsListActivity.this,couponsId);
                        finish();

                    } catch (Exception ex)
                    {
                        System.out.println(ex.getMessage());
                    }finally{
                        if(null!=dao)
                            dao.closeConnection();
                    }
                }else if(AppSharedPreference.getBoolean(DealsListActivity.this,couponId)){
                    AppDao dao=null;
                    try {
                        dao=AppDao.getSingleton(getApplicationContext());
                        dao.openConnection();
                        dao.updateDealOrder();
                        Toast.makeText(DealsListActivity.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();

                        ArrayList<DealOrderVo>dealOrderVos1= dao.getDealOrdersList();
                        int i=dealOrderVos1.size();
                        double tota=0.00;
                        for (int x=0;x<dealOrderVos1.size();x++){
                            tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
                        }
                        TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
                        if (null != dealOrderVos1 && dealOrderVos1.size() > 0) {
                            itemsPrice.setText(i+" Items "+"\n$" + tota);
                            itemsPrice.setVisibility(View.VISIBLE);
                        }
                        else{
                            itemsPrice.setVisibility(View.INVISIBLE);

                        }
                        AppSharedPreference.putBoolean(DealsListActivity.this,couponsId+"",false);
                        finish();

                    } catch (Exception ex)
                    {
                        System.out.println(ex.getMessage());
                    }finally{
                        if(null!=dao)
                            dao.closeConnection();
                    }
                }else {

                    Toast.makeText(DealsListActivity.this, "Select product from deal groups", Toast.LENGTH_LONG).show();
                }
            }
        });
	}


    private void gotoActivity(String prodCatCode) {
        if(prodCatCode.equalsIgnoreCase("Pizza")){
            Intent i=new Intent(DealsListActivity.this, PizzaDetailsActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("selectedProduct",selectedBean);
            i.putExtras(bundle);
            startActivityForResult(i, 112233);
        }else if (prodCatCode.equalsIgnoreCase("Drinks"))
        {

            Intent i=new Intent(DealsListActivity.this, FavAddActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("itemName",selectedBean.getDisplayName());
            bundle.putSerializable("prodBean",selectedBean);
            //bundle.putString("catType",catType);
            i.putExtras(bundle);
            startActivityForResult(i, 112233);
        }
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

	private class MyListAdapterFav extends ArrayAdapter<CouponGroupsVo> {

		private ArrayList<CouponGroupsVo> items;

		public MyListAdapterFav(Context context, int viewResourceId,
				ArrayList<CouponGroupsVo> items) {
			super(context, viewResourceId, items);
			this.items = items;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.line_item_deal, null);
			}
            CouponGroupsVo event = items.get(position);
			if (event != null) {

				TextView title = (TextView) convertView.findViewById(R.id.textView1);
				ImageView imageView=(ImageView)convertView.findViewById(R.id.imageViewDealDone);
                ImageView icon = (ImageView) convertView.findViewById(R.id.imageView1);
                String imgPath=AppProperties.IMAGES_LOCATION;
                imageLoader.DisplayImage(imgPath+"noimage.png",DealsListActivity.this, icon);
                if(arrayListProductName.size()>0){
                    for (int i=0;i<arrayListProductName.size();i++){

                        if(event.getCouponGroupID().equalsIgnoreCase(arrayListProductName.get(i).get(2))){
                            title.setText(arrayListProductName.get(i).get(0));
                            imgPath=AppProperties.IMAGES_LOCATION;
                            if(AppProperties.isNull(arrayListProductName.get(i).get(1))){
                                imgPath=imgPath+"noimage.png";
                            }
                            else{
                                imgPath=imgPath+arrayListProductName.get(i).get(1);
                            }
                            imageLoader.DisplayImage(imgPath,DealsListActivity.this, icon);
                            imageView.setVisibility(View.VISIBLE);
                            break;
                        }else {
                            imgPath=AppProperties.IMAGES_LOCATION;
                            icon = (ImageView) convertView.findViewById(R.id.imageView1);
                            imageLoader.DisplayImage(imgPath+"noimage.png",DealsListActivity.this, icon);
                            title.setText("select product #"+(position+1));
                            imageView.setVisibility(View.INVISIBLE);
                        }
                    }
                }  else {
				    title.setText("select product #"+(position+1));
                    imageView.setVisibility(View.INVISIBLE);
                }

//				price.setText("$"+AppProperties.getRoundTwoDecimalString(event.getPrice()));
//				calories.setText(event.getCaloriesQty()+"kj");

				

				
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
        ArrayList<DealOrderVo>dealOrderVos1= dao.getDealOrdersList();
        arrayListProductName=new ArrayList<ArrayList<String>>();
        double total=0;
        if(couponsId.size()>0){
            for (int i=0;i<couponsId.size();i++){
                if(dao.getDealData(couponsId.get(i),couponId).size()>0){
                    arrayListProductName.add(dao.getDealData(couponsId.get(i),couponId));
                }
                Log.d("................",arrayListProductName.toString());
            }
        }
        if(arrayListProductName.size()>0){
            for(int i=0;i<arrayListProductName.size();i++){
                total+=(Double.parseDouble(arrayListProductName.get(i).get(3))) * (Double.parseDouble(arrayListProductName.get(i).get(4)) );
            }
        }
        TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
        TextView textView=(TextView)findViewById(R.id.textViewPrice);
        double tota=0.00;
        int dealCount=0;
        if((dealOrderVos1!=null && dealOrderVos1.size()>0)){
            dealCount=dealOrderVos1.size();
            for (int x=0;x<dealOrderVos1.size();x++){
                tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
            }
        }

        textView.setText("$"+total);
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

        listview.invalidate();
        myAdapter = new MyListAdapterFav(this,R.layout.line_item_deal, allProductsList);
        listview.setAdapter(myAdapter);


    } catch (Exception ex) {
        System.out.println(ex.getMessage());
    } finally {
        if (null != dao)
            dao.closeConnection();
    }
    // ///////////////////////////////////////////////////////////////////////

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
//comment = new api implementation to get coupon groups for coupon id
	HttpGet httpGet = new HttpGet(AppProperties.WEB_SERVICE_PATH+"/GetCouponGroups.aspx?CouponID="+couponId);
    Log.d("req..deal....",AppProperties.WEB_SERVICE_PATH+"/GetCouponGroups.aspx?CouponID="+couponId);
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
        if (statusCode==200){
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
      
      ArrayList<CouponGroupsVo> couponDetails = new ArrayList<CouponGroupsVo>();

      if(dataExists==true)
      {
          CouponGroupsVo aBean;
	      for(int i=0; i<resultsArray.length(); i++){
	    	  JSONObject jsResult = resultsArray.getJSONObject(i);
	          if(jsResult!=null){
	                String jsonString = jsResult.toString();
	                aBean=new CouponGroupsVo();
	                aBean=gson.fromJson(jsonString, CouponGroupsVo.class);
	//                System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
	                couponDetails.add(aBean);
	          }
	     }
      }
       allProductsList=couponDetails;
      System.out.println(couponDetails.size());
        }else {
            allProductsList=new ArrayList<CouponGroupsVo>();
        }
//
//
//		AppDao dao=null;
//		try {
//			dao=AppDao.getSingleton(getApplicationContext());
//			dao.openConnection();
//			allProductsList=new ArrayList<AllProductsVo>();
//			AllProductsVo tempProd=null;
//		      for (CouponGroupsVo couponDetailsVo : couponDetails) {
//		    	  	tempProd = dao.getProductById(couponDetailsVo.getCouponID());
//		    	  	if(tempProd != null)
//		    	  	{
//		    	  		tempProd.setPrice(couponDetailsVo.getDiscountedPrice());
//		    	  		allProductsList.add(tempProd);
//		    	  	}
//		      }
//
//
//		} catch (Exception ex)
//		{
//			System.out.println(ex.getMessage());
//		}finally{
//			if(null!=dao)
//				dao.closeConnection();
//		}

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
		if (allProductsList.size()>0) {
            numOfDeals=allProductsList.size();
            listview = (ListView) findViewById(R.id.listView1);
            myAdapter = new MyListAdapterFav(this,R.layout.line_item_deal, allProductsList);
            listview.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
            AppSharedPreference.putInteger(DealsListActivity.this, "numDeals", allProductsList.size());
        }
        for(int i=0;i<allProductsList.size();i++){
            AppSharedPreference.putBoolean(DealsListActivity.this, allProductsList.get(i).getCouponGroupID(), false);
            couponsId.add(allProductsList.get(i).getCouponGroupID());
        }


	} catch (Exception e){
		e.printStackTrace();
	}
	
}

//rukshan add
final Handler mHandler = new Handler();
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            updateResultsInUi();
        }
    };


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



            if(dataExists==true)
            {
                CouponsVo aBean;
                for(int i=0; i<resultsArray.length(); i++){
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    if(jsResult!=null){
                       prodCatCode=jsResult.getString("ProdCatCode");
                    }
                }
            }

            gotoActivity(prodCatCode);

        } catch (Exception e){
            e.printStackTrace();
            AlertDialog alertDialog = new AlertDialog.Builder(DealsListActivity.this).create();
            alertDialog.setTitle("Login");
            alertDialog.setMessage(e.getMessage());
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                } });
            alertDialog.show();
        }

    }
    String serverResponse;
    protected void getCatagoryName(String catID) {
        pd = ProgressDialog.show(DealsListActivity.this, "", "Please wait...", true, false);

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
    public void startLongRunningOperation() {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(AppProperties.WEB_SERVICE_PATH+"/GetCouponDetail.aspx?CouponID"+productCatId);
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
}

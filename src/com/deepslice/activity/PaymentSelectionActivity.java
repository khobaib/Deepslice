package com.deepslice.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepslice.cache.ImageLoader;
import com.deepslice.database.AppDao;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.Customer;
import com.deepslice.model.CustomerInfo;
import com.deepslice.model.LocationDetails;
import com.deepslice.model.OrderInfo;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;
import com.deepslice.utilities.Utils;

public class PaymentSelectionActivity extends Activity{
	
	TextView tvTotalPrice;
	ImageView Banner;
	
	ImageLoader imageLoader;
	
	DeepsliceApplication appInstance;
//    TextView tvItemsPrice, tvFavCount;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_options);
		
		tvTotalPrice = (TextView)findViewById(R.id.totalPrice);
		
		appInstance = (DeepsliceApplication) getApplication();
		imageLoader = new ImageLoader(PaymentSelectionActivity.this, 400);
		
		Banner = (ImageView) findViewById(R.id.iv_banner);	
		
		String url = "http://apps.deepslice.com.au/banners/banner2.png";
		imageLoader.DisplayImage(url, Banner);
		
		LocationDetails selectedLocation = AppProperties.getLocationObj(PaymentSelectionActivity.this);
		Log.d(">>>>>>>>>>>", "street name = " + selectedLocation.getStreetName());
		
        int orderItemCount = 0;
        String orderPrice = "0.0";
        List<String> orderInfo = Utils.OrderInfo(PaymentSelectionActivity.this);
        if(orderInfo != null && orderInfo.size() == 2){
            orderPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);
            orderItemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        }
        
        
        CustomerInfo custInfo = appInstance.loadCustomerInfo();
        Customer customer = appInstance.loadCustomer();
        
        custInfo.setCustomerID(customer.getCustomerID());
        custInfo.setCustomerPhone(customer.getPhoneNo());
        custInfo.setCustomerName(customer.getCustomerName());
        custInfo.setCustomerPassword(customer.getPwd());
        custInfo.setSuburbID(Integer.parseInt(selectedLocation.getSuburbId()));
        custInfo.setPostalCode(selectedLocation.getPostalCode());
        custInfo.setUNIT(selectedLocation.getUnit());
        custInfo.setStreetName(selectedLocation.getStreetName());
        custInfo.setCrossStreet(selectedLocation.getCrossStreetName());
        custInfo.setDeliveryInstructions(selectedLocation.getDeliveryInstructions());
        custInfo.setMailingAddress(customer.getEmailID());
        
        appInstance.saveCustomerInfo(custInfo);
        
        OrderInfo oInfo = appInstance.loadOrderInfo();
        oInfo.setLocationID(Integer.parseInt(selectedLocation.getLocationID()));
        oInfo.setCustomerID(customer.getCustomerID());
        oInfo.setServiceMethod(appInstance.getOrderType());
        oInfo.setTotalPrice(Double.parseDouble(orderPrice));
        oInfo.setNoOfItems(orderItemCount);
        
        appInstance.saveOrderInfo(oInfo);
        
		
//        tvItemsPrice = (TextView) findViewById(R.id.itemPrice);
//        tvFavCount = (TextView) findViewById(R.id.favCount);
/////////////////		
		
		Button payByCash =(Button)findViewById(R.id.btnPayByCash);
		payByCash.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String location=getIntent().getStringExtra("location");
				String store=getIntent().getStringExtra("store");
				String suburbId=getIntent().getStringExtra("suburbId");
				
				
				Intent intent=new Intent();
				intent.setClass(PaymentSelectionActivity.this,PayByCashActivity.class);
				
				Bundle bundle=new Bundle();
				bundle.putString("location",location);
				bundle.putString("store",store);
				bundle.putString("suburbId",suburbId);
				intent.putExtras(bundle);			
											
				startActivity(intent);
			}
		});
		
	      Button payByCard =(Button)findViewById(R.id.btnPayByCard);
	      payByCard.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                
	                String location=getIntent().getStringExtra("location");
	                String store=getIntent().getStringExtra("store");
	                String suburbId=getIntent().getStringExtra("suburbId");
	                
	                
	                Intent intent=new Intent();
	                intent.setClass(PaymentSelectionActivity.this, PayByCardActivity.class);
	                
	                Bundle bundle=new Bundle();
	                bundle.putString("location",location);
	                bundle.putString("store",store);
	                bundle.putString("suburbId",suburbId);
	                intent.putExtras(bundle);           
	                                            
	                startActivity(intent);
	            }
	        });
	}
	
	
    @Override
    protected void onResume() {
        super.onResume();
        List<String> orderInfo = Utils.OrderInfo(PaymentSelectionActivity.this);
        int itemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        String totalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);
        
        tvTotalPrice.setText("$" + totalPrice);
        
//        if(itemCount > 0){
//            tvItemsPrice.setVisibility(View.VISIBLE);
//            tvItemsPrice.setText(itemCount + " Items "+"\n$" + totalPrice);
//
//        }
//        else{
//            tvItemsPrice.setVisibility(View.INVISIBLE);
//        }
//
//        
//        String favCount = Utils.FavCount(PaymentSelectionActivity.this);
//        if (favCount != null && !favCount.equals("0")) {
//            tvFavCount.setText(favCount);
//            tvFavCount.setVisibility(View.VISIBLE);
//        }
//        else{
//            tvFavCount.setVisibility(View.INVISIBLE);
//        }
    }

}

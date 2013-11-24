package com.deepslice.activity;

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

import com.bugsense.trace.BugSenseHandler;
import com.deepslice.cache.ImageLoader;
import com.deepslice.model.AppInfo;
import com.deepslice.model.Customer;
import com.deepslice.model.CustomerInfo;
import com.deepslice.model.LocationDetails;
import com.deepslice.model.OrderInfo;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;
import com.deepslice.utilities.Utils;

public class PaymentSelectionActivity extends Activity{
	
	TextView tvSubTotalPrice, tvDeliveryCharge, tvTotalPrice;
	ImageView Banner;
	
	ImageLoader imageLoader;
	double orderTotalPrice;
	double deliveryCharges;
	
	DeepsliceApplication appInstance;
//    TextView tvItemsPrice, tvFavCount;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(this, "92b170cf");
        
		setContentView(R.layout.payment_selection);
		
		tvSubTotalPrice = (TextView)findViewById(R.id.tv_subtotal_price);
		tvDeliveryCharge = (TextView)findViewById(R.id.tv_delivery_price);
		tvTotalPrice = (TextView)findViewById(R.id.tv_total_price);
		
		appInstance = (DeepsliceApplication) getApplication();
		imageLoader = new ImageLoader(PaymentSelectionActivity.this, 400);
		
		Banner = (ImageView) findViewById(R.id.iv_banner);	
		
		String url = "http://apps.deepslice.com.au/banners/banner2.png";
		imageLoader.DisplayImage(url, Banner);
		
		LocationDetails selectedLocation = AppProperties.getLocationObj(PaymentSelectionActivity.this);
		Log.d(">>>>>>>>>>>", "street name = " + selectedLocation.getStreetName());
		
        int orderItemCount = 0;
        orderTotalPrice = 0.0;
        List<String> orderInfo = Utils.OrderInfo(PaymentSelectionActivity.this);
        if(orderInfo != null && orderInfo.size() == 2){
            orderTotalPrice = Double.parseDouble(orderInfo.get(Constants.INDEX_ORDER_PRICE));
            orderItemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        }
        
        deliveryCharges = 0.0;
        if(appInstance.getOrderType() == Constants.ORDER_TYPE_DELIVERY){
            AppInfo appInfo = appInstance.loadAppInfo();
            deliveryCharges = Double.parseDouble(appInfo.getDeliveryCharges());
            orderTotalPrice+= deliveryCharges;
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
        oInfo.setTotalPrice(orderTotalPrice);
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
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        BugSenseHandler.startSession(this);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        BugSenseHandler.closeSession(this);
    }
	
	
    @Override
    protected void onResume() {
        super.onResume();
        List<String> orderInfo = Utils.OrderInfo(PaymentSelectionActivity.this);
//        int itemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        String subTotalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);
        
        tvSubTotalPrice.setText("$" + subTotalPrice);
        tvTotalPrice.setText("$" + Constants.twoDForm.format(orderTotalPrice));
        
//        AppInfo appInfo = appInstance.loadAppInfo();
//        double deliveryCharges = Double.parseDouble(appInfo.getDeliveryCharges());
        tvDeliveryCharge.setText("$" + Constants.twoDForm.format(deliveryCharges));
        
    }

}

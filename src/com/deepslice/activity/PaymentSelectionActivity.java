package com.deepslice.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.deepslice.database.AppDao;
import com.deepslice.utilities.AppProperties;

public class PaymentSelectionActivity extends Activity{
	
	TextView totalPrice;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_options);
		
		totalPrice=(TextView)findViewById(R.id.totalPrice);
/////////////////		
		
		Button addCoupon=(Button)findViewById(R.id.btnPayByCash);
		addCoupon.setOnClickListener(new OnClickListener() {
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
				
				
				
				
				
				
//				finish();
			}
		});
		///////////////////////////////
		AppDao dao=null;
		try {
			dao=AppDao.getSingleton(getApplicationContext());
			dao.openConnection();
		
			ArrayList<String> orderInfo = dao.getOrderInfo();
			
			if(null!=orderInfo && orderInfo.size()==2)
			{
				totalPrice.setText("$"+AppProperties.getRoundTwoDecimalString(orderInfo.get(1)));
			}

		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}finally{
			if(null!=dao)
				dao.closeConnection();
		}
		
		////////////////////////////////////////////////////////////////

	}

}

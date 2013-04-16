package com.deepslice.activity;

import com.deepslice.database.HelperSharedPreferences;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.vo.UserBean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class PickupDeliverActivity extends Activity implements OnClickListener {

	Button pickUpButton;
	Button deliverButton;
	Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_up_or_delivery);

		pickUpButton = (Button) findViewById(R.id.pickUpButton);
		deliverButton = (Button) findViewById(R.id.deliveryButton);
		loginButton = (Button) findViewById(R.id.loginButton);
		RelativeLayout footerRelative = (RelativeLayout)findViewById(R.id.footerRelativeLayout);
		if(!HelperSharedPreferences.getSharedPreferencesString(PickupDeliverActivity.this,"emailName","").equals("")||!HelperSharedPreferences.getSharedPreferencesString(PickupDeliverActivity.this,"userName","").equals(""))
		{
			footerRelative.setVisibility(View.INVISIBLE);
		}
		pickUpButton.setOnClickListener(this);
		deliverButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.loginButton:
			startActivity(new Intent(PickupDeliverActivity.this, LoginActivity.class));
			break;
		case R.id.pickUpButton:
			AppSharedPreference.putData(PickupDeliverActivity.this, "orderType", "Pickup");
			startActivity(new Intent(this, MenuActivity.class));
			break;
		case R.id.deliveryButton:
			AppSharedPreference.putData(PickupDeliverActivity.this, "orderType", "Delivery");
			startActivity(new Intent(this, MenuActivity.class));
			break;
		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		
		if(AppProperties.isLoogedIn==false){
		UserBean savedUserObj=AppProperties.getUserFromSession(getApplicationContext());
        if(null != savedUserObj && !AppProperties.isNull(savedUserObj.getCustomerID()))
        	{
        	AppProperties.userObj=savedUserObj;
        	AppProperties.isLoogedIn=true;
        	}
		}
		if(AppProperties.isLoogedIn==true)
			{
			RelativeLayout footerRelativeLayout = (RelativeLayout) findViewById(R.id.footerRelativeLayout);
			footerRelativeLayout.setVisibility(View.INVISIBLE);
			}
		super.onResume();
	}
}

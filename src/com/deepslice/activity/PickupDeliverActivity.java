package com.deepslice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.deepslice.database.HelperSharedPreferences;
import com.deepslice.model.UserBean;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;

public class PickupDeliverActivity extends Activity implements OnClickListener {

	Button pickUpButton;
	Button deliverButton;
	Button loginButton;
    TextView footerText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_up_or_delivery);

		pickUpButton = (Button) findViewById(R.id.pickUpButton);
		deliverButton = (Button) findViewById(R.id.deliveryButton);
		loginButton = (Button) findViewById(R.id.loginButton);
		RelativeLayout footerRelative = (RelativeLayout)findViewById(R.id.footerRelativeLayout);
        footerText=(TextView)findViewById(R.id.footerText);
  //comment = add new image for button logout
		if(!HelperSharedPreferences.getSharedPreferencesString(PickupDeliverActivity.this,"emailName","").equals("")||!HelperSharedPreferences.getSharedPreferencesString(PickupDeliverActivity.this,"userName","").equals(""))
		{
			footerRelative.setVisibility(View.VISIBLE);
              footerText.setVisibility(View.INVISIBLE);
            loginButton.setBackgroundResource(R.drawable.logout);
            loginButton.setTag("1");
		} else {
            loginButton.setTag("0");
        }
		pickUpButton.setOnClickListener(this);
		deliverButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.loginButton:
            if(view.getTag()=="1" ){
//               // AppProperties.isLoogedIn=false;
                HelperSharedPreferences.putSharedPreferencesString(PickupDeliverActivity.this,"emailName","");
                HelperSharedPreferences.putSharedPreferencesString(PickupDeliverActivity.this,"userName","");
                AppSharedPreference.putData(PickupDeliverActivity.this,"customerName","");
                AppSharedPreference.putData(PickupDeliverActivity.this,"customerPhone","");
                AppSharedPreference.putData(PickupDeliverActivity.this,"customerEmail","");
                AppSharedPreference.removeUserSession(getApplicationContext());
                AppProperties.isLoogedIn=false;
                loginButton.setBackgroundResource(R.drawable.btn_login);
                loginButton.setTag("0");
                footerText.setVisibility(View.VISIBLE);

            }  else {
			startActivity(new Intent(PickupDeliverActivity.this, LoginActivity.class));
                loginButton.setTag("1");
            }
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
			footerRelativeLayout.setVisibility(View.VISIBLE);
                footerText.setVisibility(View.INVISIBLE);
                loginButton.setBackgroundResource(R.drawable.logout);
                loginButton.setTag("1");

			}
		super.onResume();
	}
}

package com.deepslice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deepslice.model.Customer;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;

public class PickupDeliverActivity extends Activity implements OnClickListener {

    Button pickUpButton;
    Button deliverButton;
    Button loginButton;
    TextView footerText;
    
    DeepsliceApplication appInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_up_or_delivery);
        
        appInstance = (DeepsliceApplication) getApplication();

        pickUpButton = (Button) findViewById(R.id.pickUpButton);
        deliverButton = (Button) findViewById(R.id.deliveryButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        RelativeLayout footerRelative = (RelativeLayout)findViewById(R.id.footerRelativeLayout);
        footerText=(TextView)findViewById(R.id.footerText);
        //comment = add new image for button logout

        Customer customer = appInstance.loadCustomer();
        if(customer.getCustomerID() != 0) {      // remember me ON
//        if(!HelperSharedPreferences.getSharedPreferencesString(PickupDeliverActivity.this,"emailName", "").equals("")
//                || !HelperSharedPreferences.getSharedPreferencesString(PickupDeliverActivity.this,"userName", "").equals("")) {
            footerRelative.setVisibility(View.VISIBLE);
            footerText.setVisibility(View.INVISIBLE);
            loginButton.setBackgroundResource(R.drawable.logout);
//            loginButton.setTag("1");
            AppProperties.isLoggedIn = true;
        } else {
//            loginButton.setTag("0");
            AppProperties.isLoggedIn = false;
        }
        
        pickUpButton.setOnClickListener(this);
        deliverButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                if(AppProperties.isLoggedIn == true){            // already logged-in, so signing out.
                    Customer customer = new Customer(0, null, null, null, null, false);
                    appInstance.saveCustomer(customer);
                    appInstance.setRememberMe(false);
                    //               // AppProperties.isLoogedIn=false;
//                    HelperSharedPreferences.putSharedPreferencesString(PickupDeliverActivity.this,"emailName","");
//                    HelperSharedPreferences.putSharedPreferencesString(PickupDeliverActivity.this,"userName","");
//                    AppSharedPreference.putData(PickupDeliverActivity.this,"customerName","");
//                    AppSharedPreference.putData(PickupDeliverActivity.this,"customerPhone","");
//                    AppSharedPreference.putData(PickupDeliverActivity.this,"customerEmail","");
//                    AppSharedPreference.removeUserSession(getApplicationContext());
                    AppProperties.isLoggedIn = false;
                    loginButton.setBackgroundResource(R.drawable.btn_login);
//                    loginButton.setTag("0");
                    footerText.setVisibility(View.VISIBLE);

                }  else {
                    appInstance.setOrderReady(false);
                    startActivity(new Intent(PickupDeliverActivity.this, LoginActivity.class));
//                    loginButton.setTag("1");
                }
                break;
            case R.id.pickUpButton:
                appInstance.setOrderType(Constants.ORDER_TYPE_PICKUP);
//                AppSharedPreference.putData(PickupDeliverActivity.this, "orderType", "Pickup");
                startActivity(new Intent(this, MainMenuActivity.class));
                break;
            case R.id.deliveryButton:
                appInstance.setOrderType(Constants.ORDER_TYPE_DELIVERY);
//                AppSharedPreference.putData(PickupDeliverActivity.this, "orderType", "Delivery");
                startActivity(new Intent(this, MainMenuActivity.class));
                break;
            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        
        if(AppProperties.isLoggedIn == false){

//            Customer savedUserObj = AppProperties.getUserFromSession(getApplicationContext());
//            if(null != savedUserObj && !AppProperties.isNull(savedUserObj.getCustomerID())) {
//                AppProperties.userObj=savedUserObj;
//                AppProperties.isLoggedIn=true;
//            }
            Customer customer = appInstance.loadCustomer();
            if(customer.getCustomerID() != 0) {
//                AppProperties.userObj=savedUserObj;
                AppProperties.isLoggedIn = true;
            }
        }
        
        if(AppProperties.isLoggedIn == true){
            RelativeLayout footerRelativeLayout = (RelativeLayout) findViewById(R.id.footerRelativeLayout);
            footerRelativeLayout.setVisibility(View.VISIBLE);
            footerText.setVisibility(View.INVISIBLE);
            loginButton.setBackgroundResource(R.drawable.logout);
//            loginButton.setTag("1");

        }
    }
    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { // Back key pressed
            if(!appInstance.getIsRememberMe()){
                Customer customer = new Customer(0, null, null, null, null, false);
                appInstance.saveCustomer(customer);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

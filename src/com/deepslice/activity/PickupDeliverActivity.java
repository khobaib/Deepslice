package com.deepslice.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bugsense.trace.BugSenseHandler;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.Customer;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;

public class PickupDeliverActivity extends Activity implements OnClickListener {

    private static final int BUTTON_POSITIVE = -1;
    private static final int BUTTON_NEGATIVE = -2;

    Button pickUpButton;
    Button deliverButton;
    Button loginButton;
    TextView footerText;

    DeepsliceApplication appInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BugSenseHandler.initAndStartSession(PickupDeliverActivity.this, "92b170cf");
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
            footerRelative.setVisibility(View.VISIBLE);
            footerText.setVisibility(View.INVISIBLE);
            loginButton.setBackgroundResource(R.drawable.logout);
            AppProperties.isLoggedIn = true;
        } else {
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
                    AppProperties.isLoggedIn = false;
                    loginButton.setBackgroundResource(R.drawable.btn_login);
                    footerText.setVisibility(View.VISIBLE);

                }  else {
                    appInstance.setOrderReady(false);
                    startActivity(new Intent(PickupDeliverActivity.this, LoginActivity.class));
                }
                break;
            case R.id.pickUpButton:
                int currentOrderType = appInstance.getOrderType();
                if(currentOrderType == Constants.ORDER_TYPE_DELIVERY){
                    alertOrderMethodToChange(Constants.ORDER_TYPE_PICKUP);
                }
                else{
                    appInstance.setOrderType(Constants.ORDER_TYPE_PICKUP);
                    startActivity(new Intent(PickupDeliverActivity.this, MainMenuActivity.class));
                }
                break;
            case R.id.deliveryButton:
                currentOrderType = appInstance.getOrderType();
                if(currentOrderType == Constants.ORDER_TYPE_PICKUP){
                    alertOrderMethodToChange(Constants.ORDER_TYPE_DELIVERY);
                }
                else{
                    appInstance.setOrderType(Constants.ORDER_TYPE_DELIVERY);
                    startActivity(new Intent(PickupDeliverActivity.this, MainMenuActivity.class));
                }
                break;
            default:
                break;
        }

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
            BugSenseHandler.closeSession(PickupDeliverActivity.this);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void alertOrderMethodToChange(final int orderType){


        AlertDialog alert = new AlertDialog.Builder(PickupDeliverActivity.this).create(); 
        alert.setCancelable(false);
        alert.setTitle("Caution!");
        alert.setMessage("You are about to change ordering method. All previous order will be deleted."); 

        alert.setButton(BUTTON_POSITIVE, "OK, Proceed", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(PickupDeliverActivity.this);
                dbInstance.open();
                dbInstance.cleanAllOrderTable();
                dbInstance.close(); 

                appInstance.setOrderReady(false);
                appInstance.setOrderType(orderType);
                
                startActivity(new Intent(PickupDeliverActivity.this, MainMenuActivity.class));
            }
        });

        alert.setButton(BUTTON_NEGATIVE, "Keep this order", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });            
        alert.show();
    }
}

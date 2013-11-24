package com.deepslice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.deepslice.database.HelperSharedPreferences;
import com.deepslice.model.Customer;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;

public class CustomerDetailsActivity extends Activity implements OnClickListener {

    EditText nameEditText;
    EditText emailEditText;
    EditText phoneEditText;
    Button registerButton;
    Button startOrtderButton;
    CheckBox sendEmailCheckBox;
    private Button btnLog;

    DeepsliceApplication appInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(this, "92b170cf");
        
        setContentView(R.layout.customer_details);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        appInstance = (DeepsliceApplication) getApplication();

        initializeAllViews();

        //        ImageView emgBack = (ImageView)findViewById(R.id.farwordImageView);
        //        emgBack.setOnClickListener(new OnClickListener() {
        //            public void onClick(View v) {
        //                finish();
        //            }
        //        });

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
    protected void onResume(){
        super.onResume();

    }
    private void initializeAllViews() {

        nameEditText = (EditText) findViewById(R.id.yourNameEditText);
        emailEditText = (EditText) findViewById(R.id.yourEmailEditText);
        phoneEditText = (EditText) findViewById(R.id.yourPhoneEditText);
        registerButton = (Button) findViewById(R.id.registerButton);
        startOrtderButton = (Button) findViewById(R.id.startOrderingButton);
        sendEmailCheckBox = (CheckBox) findViewById(R.id.checkBox);
        registerButton.setOnClickListener(this);
        startOrtderButton.setOnClickListener(this);

        //        		Boolean isOrderReady = AppSharedPreference.getBoolean(CustomerDetailsActivity.this, "MyOrder");
        Boolean isOrderReady = appInstance.getIsOrderReady();
        if(!isOrderReady)
            startOrtderButton.setText("Start Order");

        btnLog=(Button)findViewById(R.id.bLog);
        btnLog.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(CustomerDetailsActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                //				Intent intent=new Intent(CustomerDetailsActivity.this,LoginActivity.class);
                //				startActivity(intent);
            }
        });

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.registerButton:
                if (isRegInfoInserted()) {
                    appInstance.saveCustomer(getCustomerDetails());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.CUSTOMER_DETAILS, getCustomerDetails());
                    //                    HelperSharedPreferences.putSharedPreferencesString(CustomerDetailsActivity.this, "userName", nameEditText.getText().toString());
                    //                    HelperSharedPreferences.putSharedPreferencesString(CustomerDetailsActivity.this,"emailName" ,emailEditText.getText().toString());
                    //                    HelperSharedPreferences.putSharedPreferencesLong(CustomerDetailsActivity.this,"phoneNo" ,Long.parseLong(phoneEditText.getText().toString()));
                    //                    HelperSharedPreferences.putSharedPreferencesBoolean(CustomerDetailsActivity.this,"sendEmailCheckbox" ,sendEmailCheckBox.isChecked());
                    //                    AppSharedPreference.putData(CustomerDetailsActivity.this, "customerName", nameEditText.getText().toString());
                    //                    AppSharedPreference.putData(CustomerDetailsActivity.this, "customerEmail", emailEditText.getText().toString());
                    //                    AppSharedPreference.putData(CustomerDetailsActivity.this, "customerPhone", phoneEditText.getText().toString());
                    startActivity(new Intent(this, RegisterActivity.class).putExtras(bundle));

                }
                break;

            case R.id.startOrderingButton:
                if (isOrderingInfoInserted()){
                    appInstance.saveCustomer(getCustomerDetails());
                    //                    AppSharedPreference.putData(CustomerDetailsActivity.this, "customerName", nameEditText.getText().toString());
                    //                    AppSharedPreference.putData(CustomerDetailsActivity.this, "customerEmail", emailEditText.getText().toString());
                    //                    AppSharedPreference.putData(CustomerDetailsActivity.this, "customerPhone", phoneEditText.getText().toString());

                    Boolean isOrderReady = appInstance.getIsOrderReady();
                    if (isOrderReady){
                        //                        AppSharedPreference.putBoolean(RegisterActivity.this, "MyOrder",false);
                        int orderType = appInstance.getOrderType();
                        if(orderType == Constants.ORDER_TYPE_DELIVERY) {
                            startActivity(new Intent(new Intent(this, LocationFromHistoryActivity.class)));
                            finish();
                        }else {
                            startActivity(new Intent(new Intent(this, StoreFromHistoryActivity.class)));
                            finish();
                        }

                    }  else {                       
                        startActivity(new Intent(new Intent(this,PickupDeliverActivity.class)));
                        finish();
                    }
                }
                break;

            default:
                break;
        }

    }

    private Customer getCustomerDetails() {
        Customer customerDetails = new Customer();
        customerDetails.setCustomerName(nameEditText.getText().toString().trim());
        customerDetails.setEmailID(emailEditText.getText().toString().trim());
        customerDetails.setPhoneNo(phoneEditText.getText().toString().trim());
        customerDetails.setSendEmail(sendEmailCheckBox.isSelected());
        return customerDetails;
    }


    // checking user-registration info
    private boolean isRegInfoInserted() {

        if (nameEditText.getText().toString() != null && nameEditText.getText().toString().trim().length() > 0) {
            if (emailEditText.getText().toString().trim() != null && emailEditText.getText().toString().trim().length() > 0) {
                if (phoneEditText.getText().toString() != null && phoneEditText.getText().toString().trim().length() == 10) {
                    return true;
                } else {
                    Toast.makeText(this, R.string.phoneErrorMessage, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, R.string.emailErrorMessage, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.nameErrorMessage, Toast.LENGTH_LONG).show();
        }
        return false;
    }


    // user can proceed to order w/o email inserted
    private boolean isOrderingInfoInserted() {

        if (nameEditText.getText().toString() != null && nameEditText.getText().toString().trim().length() > 0) {
            if (emailEditText.getText().toString().trim() != null) {

                if (phoneEditText.getText().toString() != null && phoneEditText.getText().toString().trim().length() == 10) {
                    return true;
                } else {
                    Toast.makeText(this, R.string.phoneErrorMessage, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, R.string.emailErrorMessage, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.nameErrorMessage, Toast.LENGTH_LONG).show();
        }
        return false;
    }
}

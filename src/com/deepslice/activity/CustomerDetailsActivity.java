package com.deepslice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.*;
import com.deepslice.database.HelperSharedPreferences;
import com.deepslice.model.CustomerDetails;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.utilities.Constants;

public class CustomerDetailsActivity extends Activity implements
		OnClickListener {

	EditText nameEditText;
	EditText emailEditText;
	EditText phoneEditText;
	Button registerButton;
	Button startOrtderButton;
	CheckBox sendEmailCheckBox;
	private Button btnLog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_details);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initializeAllViews();

		ImageView emgBack = (ImageView)findViewById(R.id.farwordImageView);
		emgBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

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
		
		btnLog=(Button)findViewById(R.id.bLog);
		btnLog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                Intent i = new Intent(CustomerDetailsActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(i, 1245678);
//				Intent intent=new Intent(CustomerDetailsActivity.this,LoginActivity.class);
//				startActivity(intent);
			}
		});

	}




	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.registerButton:
			 if (velidateregisterButton()) {

			Bundle bundle = new Bundle();
			bundle.putSerializable(Constants.CUSTOMER_DETAILS,
					getCustomerDetails());
           HelperSharedPreferences.putSharedPreferencesString(CustomerDetailsActivity.this, "userName", nameEditText.getText().toString());
           HelperSharedPreferences.putSharedPreferencesString(CustomerDetailsActivity.this,"emailName" ,emailEditText.getText().toString());
           HelperSharedPreferences.putSharedPreferencesLong(CustomerDetailsActivity.this,"phoneNo" ,Long.parseLong(phoneEditText.getText().toString()));
           HelperSharedPreferences.putSharedPreferencesBoolean(CustomerDetailsActivity.this,"sendEmailCheckbox" ,sendEmailCheckBox.isChecked());
                 AppSharedPreference.putData(CustomerDetailsActivity.this, "customerName", nameEditText.getText().toString());
                 AppSharedPreference.putData(CustomerDetailsActivity.this, "customerEmail", emailEditText.getText().toString());
                 AppSharedPreference.putData(CustomerDetailsActivity.this, "customerPhone", phoneEditText.getText().toString());
                 startActivity(new Intent(this, RegisterActivity.class)
					.putExtras(bundle));

			 }
			break;

		case R.id.startOrderingButton:
			if (velidateStartOverButton())
				{

				AppSharedPreference.putData(CustomerDetailsActivity.this, "customerName", nameEditText.getText().toString());
				AppSharedPreference.putData(CustomerDetailsActivity.this, "customerEmail", emailEditText.getText().toString());
				AppSharedPreference.putData(CustomerDetailsActivity.this, "customerPhone", phoneEditText.getText().toString());
                    String orderType = AppSharedPreference.getData(CustomerDetailsActivity.this, "orderType", null);

                    if(orderType.equalsIgnoreCase("Delivery")) {
                        startActivity(new Intent(new Intent(this,LocationFromHistoryActivity.class)));
                        finish();
                    } else {
                        startActivity(new Intent(new Intent(this,StoreFromHistoryActivity.class)));
                        finish();
                    }
				}
			break;

		default:
			break;
		}

	}

	private CustomerDetails getCustomerDetails() {
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setName(nameEditText.getText().toString().trim());
		customerDetails.setEmail(emailEditText.getText().toString().trim());
		customerDetails.setPhone(phoneEditText.getText().toString().trim());
		customerDetails.setSendEmail(sendEmailCheckBox.isSelected());
		return customerDetails;
	}

	private boolean velidateregisterButton() {

		if (nameEditText.getText().toString() != null && nameEditText.getText().toString().trim().length() > 0) {

			if (emailEditText.getText().toString().trim() != null && !(emailEditText.getText().toString().trim().equalsIgnoreCase(""))) {

				if (phoneEditText.getText().toString() != null
						&& phoneEditText.getText().toString().trim().length() == 10) {
					return true;
				} else {
					Toast.makeText(this, R.string.phoneErrorMessage, Toast.LENGTH_LONG).show();
				}

			} else {
				Toast.makeText(this, R.string.emailErrorMessage,
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, R.string.nameErrorMessage, Toast.LENGTH_LONG)
					.show();
		}
		return false;
	}

    private boolean velidateStartOverButton() {

        if (nameEditText.getText().toString() != null
                && nameEditText.getText().toString().trim().length() > 0) {

            if (emailEditText.getText().toString().trim() != null) {

                if (phoneEditText.getText().toString() != null
                        && phoneEditText.getText().toString().trim().length() == 10) {
                    return true;
                } else {
                    Toast.makeText(this, R.string.phoneErrorMessage,
                            Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, R.string.emailErrorMessage,
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.nameErrorMessage, Toast.LENGTH_LONG)
                    .show();
        }
        return false;
    }
}
